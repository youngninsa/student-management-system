package com.example.sms.controller;

import com.example.sms.common.ApiResponse;
import com.example.sms.entity.Student;
import com.example.sms.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ApiResponse<List<Student>> list(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String studentNo,
                                           @RequestParam(required = false) String major) {
        return ApiResponse.success(studentService.list(name, studentNo, major));
    }

    @GetMapping("/{id}")
    public ApiResponse<Student> detail(@PathVariable Long id) {
        return ApiResponse.success(studentService.findById(id));
    }

    @PostMapping
    public ApiResponse<Student> create(@RequestBody Student student) {
        return ApiResponse.success(studentService.create(student));
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return ApiResponse.success(studentService.update(id, student));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ApiResponse.success();
    }
}
