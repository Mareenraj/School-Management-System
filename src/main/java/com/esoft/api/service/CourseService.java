package com.esoft.api.service;

import com.esoft.api.dto.course.CourseRequest;
import com.esoft.api.dto.course.CourseResponse;
import com.esoft.api.entity.Course;
import com.esoft.api.exception.ResourceNotFoundException;
import com.esoft.api.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public CourseResponse create(CourseRequest request) {
        Course course = Course.builder()
                .name(request.name())
                .description(request.description())
                .build();
        return toResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getById(UUID id) {
        return toResponse(findCourseOrThrow(id));
    }

    @Transactional
    public CourseResponse update(UUID id, CourseRequest request) {
        Course course = findCourseOrThrow(id);
        course.setName(request.name());
        course.setDescription(request.description());
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(UUID id) {
        Course course = findCourseOrThrow(id);
        courseRepository.delete(course);
    }

    private Course findCourseOrThrow(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }

    private CourseResponse toResponse(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getDescription());
    }
}
