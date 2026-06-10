package com.example.sms.service;

import com.example.sms.entity.Student;
import com.example.sms.repository.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> list(String name, String studentNo, String major) {
        return studentRepository.findByNameContainingAndStudentNoContainingAndMajorContaining(
                safe(name), safe(studentNo), safe(major));
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student payload) {
        Student student = findById(id);
        BeanUtils.copyProperties(payload, student, "id");
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("学生不存在"));
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
