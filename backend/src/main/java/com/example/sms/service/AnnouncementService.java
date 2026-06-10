package com.example.sms.service;

import com.example.sms.entity.Announcement;
import com.example.sms.repository.AnnouncementRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public List<Announcement> list() {
        return announcementRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Announcement> latest() {
        return announcementRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public Announcement create(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    public Announcement update(Long id, Announcement payload) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("公告不存在"));
        BeanUtils.copyProperties(payload, announcement, "id", "createdAt");
        return announcementRepository.save(announcement);
    }

    public void delete(Long id) {
        announcementRepository.deleteById(id);
    }
}
