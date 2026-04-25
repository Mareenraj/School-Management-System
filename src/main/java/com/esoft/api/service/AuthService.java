package com.esoft.api.service;

import com.esoft.api.dto.auth.AuthResponse;
import com.esoft.api.dto.auth.MessageResponse;
import com.esoft.api.dto.auth.OtpRequest;
import com.esoft.api.dto.auth.RefreshTokenRequest;
import com.esoft.api.dto.auth.ResendOtpRequest;
import com.esoft.api.dto.auth.SigninRequest;
import com.esoft.api.dto.auth.SignupRequest;
import com.esoft.api.entity.User;
import com.esoft.api.entity.enums.Role;
import com.esoft.api.exception.DuplicateResourceException;
import com.esoft.api.exception.InvalidOtpException;
import com.esoft.api.exception.InvalidTokenException;
import com.esoft.api.exception.InvalidUserException;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.exception.UnverifiedAccountException;
import com.esoft.api.repository.UserRepository;
import com.esoft.api.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final String adminEmail;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       OtpService otpService,
                       EmailService emailService,
                       RefreshTokenService refreshTokenService,
                       AuthenticationManager authenticationManager,
                       @Value("${admin.email}") String adminEmail) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.adminEmail = adminEmail;
    }

    @Transactional
    public MessageResponse signup(SignupRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.email());

        if (optionalUser.isEmpty()) {
            if (request.email().equals(adminEmail)) {
                // Admin bootstrapping
                User adminUser = User.builder()
                        .name(request.name() != null ? request.name() : "Admin")
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .role(Role.ADMIN)
                        .isVerified(false)
                        .build();
                userRepository.save(adminUser);

                String otp = otpService.generateAndStoreOtp(adminUser.getEmail());
                emailService.sendOtpEmail(adminUser.getEmail(), otp, adminUser.getName());

                return new MessageResponse("Registration successful. Please check your email for OTP verification.");
            } else {
                throw new InvalidUserException("Invalid user. Contact admin.");
            }
        }

        User user = optionalUser.get();

        if (user.getPassword() != null) {
            throw new DuplicateResourceException("Account already activated.");
        }

        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        String otp = otpService.generateAndStoreOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), otp, user.getName());

        return new MessageResponse("Registration successful. Please check your email for OTP verification.");
    }

    @Transactional
    public MessageResponse verifyOtp(OtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.email()));

        if (user.isVerified()) {
            return new MessageResponse("Email is already verified.");
        }

        if (!otpService.validateOtp(request.email(), request.otp())) {
            throw new InvalidOtpException("Invalid or expired OTP");
        }

        user.setVerified(true);
        userRepository.save(user);

        return new MessageResponse("Email verified successfully. You can now sign in.");
    }

    public MessageResponse resendOtp(ResendOtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.email()));

        if (user.isVerified()) {
            return new MessageResponse("Email is already verified.");
        }

        otpService.deleteOtp(request.email());
        String otp = otpService.generateAndStoreOtp(request.email());
        emailService.sendOtpEmail(request.email(), otp, user.getName());

        return new MessageResponse("OTP has been resent to your email.");
    }

    public AuthResponse signin(SigninRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!user.isVerified()) {
            throw new UnverifiedAccountException(
                    "Email not verified. Please verify your email before signing in.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (DisabledException e) {
            throw new UnverifiedAccountException(
                    "Email not verified. Please verify your email before signing in.");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenService.storeRefreshToken(user.getEmail(), refreshToken);

        return new AuthResponse(
                accessToken,
                refreshToken,
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.refreshToken();

        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        String email = jwtService.extractEmail(token);

        if (!refreshTokenService.validateRefreshToken(email, token)) {
            throw new InvalidTokenException("Refresh token not recognized. Please sign in again.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException("User associated with token not found"));

        // Token rotation: issue new tokens and invalidate old refresh token
        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole().name());
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenService.storeRefreshToken(user.getEmail(), newRefreshToken);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }

    public MessageResponse logout(String email) {
        refreshTokenService.deleteRefreshToken(email);
        return new MessageResponse("Logged out successfully.");
    }
}
