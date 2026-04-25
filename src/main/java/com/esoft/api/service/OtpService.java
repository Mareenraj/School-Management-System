package com.esoft.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final String OTP_PREFIX = "otp:";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final RedisTemplate<String, String> redisTemplate;
    private final int otpLength;
    private final long otpExpirationSeconds;

    public OtpService(
            RedisTemplate<String, String> redisTemplate,
            @Value("${otp.length}") int otpLength,
            @Value("${otp.expiration}") long otpExpirationSeconds) {
        this.redisTemplate = redisTemplate;
        this.otpLength = otpLength;
        this.otpExpirationSeconds = otpExpirationSeconds;
    }

    public String generateAndStoreOtp(String email) {
        String otp = generateOtp();
        String key = OTP_PREFIX + email;
        redisTemplate.opsForValue().set(key, otp, otpExpirationSeconds, TimeUnit.SECONDS);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        String key = OTP_PREFIX + email;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    public void deleteOtp(String email) {
        redisTemplate.delete(OTP_PREFIX + email);
    }

    private String generateOtp() {
        int bound = (int) Math.pow(10, otpLength);
        int number = RANDOM.nextInt(bound);
        return String.format("%0" + otpLength + "d", number);
    }
}
