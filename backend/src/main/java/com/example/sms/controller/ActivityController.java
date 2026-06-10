package com.example.sms.controller;

import com.example.sms.common.ApiResponse;
import com.example.sms.dto.ActivityResponse;
import com.example.sms.dto.RegistrationResponse;
import com.example.sms.entity.Activity;
import com.example.sms.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ApiResponse<List<ActivityResponse>> list(@RequestParam(required = false) Long studentId) {
        return ApiResponse.success(activityService.list(studentId));
    }

    @PostMapping
    public ApiResponse<Activity> create(@RequestBody Activity activity) {
        return ApiResponse.success(activityService.create(activity));
    }

    @PutMapping("/{id}")
    public ApiResponse<Activity> update(@PathVariable Long id, @RequestBody Activity activity) {
        return ApiResponse.success(activityService.update(id, activity));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        activityService.delete(id);
        return ApiResponse.success();
    }

    @PostMapping("/{id}/register")
    public ApiResponse<ActivityResponse> register(@PathVariable Long id, @RequestParam Long studentId) {
        return ApiResponse.success(activityService.register(id, studentId));
    }

    @GetMapping("/{id}/registrations")
    public ApiResponse<List<RegistrationResponse>> registrations(@PathVariable Long id) {
        return ApiResponse.success(activityService.registrations(id));
    }
}
