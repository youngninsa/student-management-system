package com.example.sms.service;

import com.example.sms.repository.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AnnouncementRepository announcementRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public DashboardService(StudentRepository studentRepository,
                            CourseRepository courseRepository,
                            AnnouncementRepository announcementRepository,
                            ActivityRepository activityRepository,
                            UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.announcementRepository = announcementRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> stats() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("studentCount", studentRepository.count());
        data.put("courseCount", courseRepository.count());
        data.put("announcementCount", announcementRepository.count());
        data.put("activityCount", activityRepository.count());
        data.put("userCount", userRepository.count());
        data.put("latestAnnouncements", announcementRepository.findTop5ByOrderByCreatedAtDesc());
        return data;
    }
}
