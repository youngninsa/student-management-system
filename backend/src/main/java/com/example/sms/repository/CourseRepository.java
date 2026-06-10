package com.example.sms.repository;

import com.example.sms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCourseNameContainingOrCourseNoContainingOrTeacherNameContaining(String courseName, String courseNo, String teacherName);
}
