package com.example.sms.service;

import com.example.sms.dto.ActivityResponse;
import com.example.sms.dto.RegistrationResponse;
import com.example.sms.entity.Activity;
import com.example.sms.entity.ActivityRegistration;
import com.example.sms.entity.Student;
import com.example.sms.repository.ActivityRegistrationRepository;
import com.example.sms.repository.ActivityRepository;
import com.example.sms.repository.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityRegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;

    public ActivityService(ActivityRepository activityRepository,
                           ActivityRegistrationRepository registrationRepository,
                           StudentRepository studentRepository) {
        this.activityRepository = activityRepository;
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
    }

    public List<ActivityResponse> list(Long studentId) {
        return activityRepository.findAllByOrderByActivityTimeDesc().stream()
                .map(activity -> toResponse(activity, studentId))
                .toList();
    }

    public Activity create(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity update(Long id, Activity payload) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("活动不存在"));
        BeanUtils.copyProperties(payload, activity, "id");
        return activityRepository.save(activity);
    }

    public void delete(Long id) {
        activityRepository.deleteById(id);
    }

    @Transactional
    public ActivityResponse register(Long activityId, Long studentId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("活动不存在"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在"));
        if (registrationRepository.existsByActivityIdAndStudentId(activityId, studentId)) {
            throw new IllegalArgumentException("请勿重复报名");
        }
        long count = registrationRepository.countByActivityId(activityId);
        if (activity.getMaxCount() != null && count >= activity.getMaxCount()) {
            throw new IllegalArgumentException("报名人数已满");
        }
        ActivityRegistration registration = new ActivityRegistration();
        registration.setActivity(activity);
        registration.setStudent(student);
        registrationRepository.save(registration);
        return toResponse(activity, studentId);
    }

    public List<RegistrationResponse> registrations(Long activityId) {
        return registrationRepository.findByActivityId(activityId).stream().map(this::toRegistrationResponse).toList();
    }

    private ActivityResponse toResponse(Activity activity, Long studentId) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setTitle(activity.getTitle());
        response.setContent(activity.getContent());
        response.setLocation(activity.getLocation());
        response.setActivityTime(activity.getActivityTime());
        response.setMaxCount(activity.getMaxCount());
        response.setRegisteredCount(registrationRepository.countByActivityId(activity.getId()));
        response.setRegistered(studentId != null && registrationRepository.existsByActivityIdAndStudentId(activity.getId(), studentId));
        return response;
    }

    private RegistrationResponse toRegistrationResponse(ActivityRegistration registration) {
        RegistrationResponse response = new RegistrationResponse();
        response.setId(registration.getId());
        response.setActivityId(registration.getActivity().getId());
        response.setActivityTitle(registration.getActivity().getTitle());
        response.setStudentId(registration.getStudent().getId());
        response.setStudentNo(registration.getStudent().getStudentNo());
        response.setStudentName(registration.getStudent().getName());
        response.setCreatedAt(registration.getCreatedAt());
        return response;
    }
}
