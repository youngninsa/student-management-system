package com.example.sms.controller;

import com.example.sms.common.ApiResponse;
import com.example.sms.dto.GradeRequest;
import com.example.sms.dto.GradeResponse;
import com.example.sms.service.GradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public ApiResponse<List<GradeResponse>> list(@RequestParam(required = false) Long studentId) {
        return ApiResponse.success(gradeService.list(studentId));
    }

    @PostMapping
    public ApiResponse<GradeResponse> create(@RequestBody GradeRequest request) {
        return ApiResponse.success(gradeService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<GradeResponse> update(@PathVariable Long id, @RequestBody GradeRequest request) {
        return ApiResponse.success(gradeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ApiResponse.success();
    }
}
