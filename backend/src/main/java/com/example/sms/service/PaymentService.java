package com.example.sms.service;

import com.example.sms.dto.PaymentResponse;
import com.example.sms.entity.Payment;
import com.example.sms.entity.Student;
import com.example.sms.repository.PaymentRepository;
import com.example.sms.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    public PaymentService(PaymentRepository paymentRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
    }

    public List<PaymentResponse> list(Long studentId) {
        List<Payment> payments = studentId == null ? paymentRepository.findAll() : paymentRepository.findByStudentId(studentId);
        return payments.stream().map(this::toResponse).toList();
    }

    public PaymentResponse create(Payment payment, Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("学生不存在"));
        payment.setStudent(student);
        if (payment.getStatus() == null || payment.getStatus().isBlank()) {
            payment.setStatus("未缴费");
        }
        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse pay(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("缴费记录不存在"));
        payment.setStatus("已缴费");
        payment.setPaidAt(LocalDateTime.now());
        return toResponse(paymentRepository.save(payment));
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setStudentId(payment.getStudent().getId());
        response.setStudentNo(payment.getStudent().getStudentNo());
        response.setStudentName(payment.getStudent().getName());
        response.setItemName(payment.getItemName());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setPaidAt(payment.getPaidAt());
        return response;
    }
}
