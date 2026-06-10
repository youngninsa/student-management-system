package com.example.sms.service;

import com.example.sms.dto.GradeRequest;
import com.example.sms.dto.GradeResponse;
import com.example.sms.entity.Course;
import com.example.sms.entity.Grade;
import com.example.sms.entity.Student;
import com.example.sms.repository.CourseRepository;
import com.example.sms.repository.GradeRepository;
import com.example.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<GradeResponse> list(Long studentId) {
        List<Grade> grades = studentId == null ? gradeRepository.findAll() : gradeRepository.findByStudentId(studentId);
        return grades.stream().map(this::toResponse).toList();
    }

    public GradeResponse create(GradeRequest request) {
        Grade grade = new Grade();
        fillGrade(grade, request);
        return toResponse(gradeRepository.save(grade));
    }

    public GradeResponse update(Long id, GradeRequest request) {
        Grade grade = gradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("成绩不存在"));
        fillGrade(grade, request);
        return toResponse(gradeRepository.save(grade));
    }

    public void delete(Long id) {
        gradeRepository.deleteById(id);
    }

    private void fillGrade(Grade grade, GradeRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("学生不存在"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("课程不存在"));
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setScore(request.getScore());
        grade.setSemester(request.getSemester());
    }

    private GradeResponse toResponse(Grade grade) {
        GradeResponse response = new GradeResponse();
        response.setId(grade.getId());
        response.setStudentId(grade.getStudent().getId());
        response.setStudentNo(grade.getStudent().getStudentNo());
        response.setStudentName(grade.getStudent().getName());
        response.setCourseId(grade.getCourse().getId());
        response.setCourseNo(grade.getCourse().getCourseNo());
        response.setCourseName(grade.getCourse().getCourseName());
        response.setScore(grade.getScore());
        response.setSemester(grade.getSemester());
        response.setLevel(levelOf(grade.getScore()));
        return response;
    }

    private String levelOf(BigDecimal score) {
        if (score == null) {
            return "-";
        }
        int value = score.intValue();
        if (value >= 90) {
            return "优秀";
        }
        if (value >= 80) {
            return "良好";
        }
        if (value >= 60) {
            return "及格";
        }
        return "不及格";
    }
}
