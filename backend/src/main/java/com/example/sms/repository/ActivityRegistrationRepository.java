package com.example.sms.repository;

import com.example.sms.entity.ActivityRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, Long> {
    boolean existsByActivityIdAndStudentId(Long activityId, Long studentId);

    long countByActivityId(Long activityId);

    List<ActivityRegistration> findByActivityId(Long activityId);

    List<ActivityRegistration> findByStudentId(Long studentId);
}
