package com.example.sms.service;

import com.example.sms.entity.Course;
import com.example.sms.repository.CourseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> list(String keyword) {
        String value = keyword == null ? "" : keyword;
        return courseRepository.findByCourseNameContainingOrCourseNoContainingOrTeacherNameContaining(value, value, value);
    }

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public Course update(Long id, Course payload) {
        Course course = findById(id);
        BeanUtils.copyProperties(payload, course, "id");
        return courseRepository.save(course);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("课程不存在"));
    }
}
