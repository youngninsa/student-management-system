package com.example.sms.controller;

import com.example.sms.common.ApiResponse;
import com.example.sms.entity.Course;
import com.example.sms.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ApiResponse<List<Course>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(courseService.list(keyword));
    }

    @PostMapping
    public ApiResponse<Course> create(@RequestBody Course course) {
        return ApiResponse.success(courseService.create(course));
    }

    @PutMapping("/{id}")
    public ApiResponse<Course> update(@PathVariable Long id, @RequestBody Course course) {
        return ApiResponse.success(courseService.update(id, course));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ApiResponse.success();
    }
}
