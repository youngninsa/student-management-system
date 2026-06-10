package com.example.sms.controller;

import com.example.sms.common.ApiResponse;
import com.example.sms.dto.PaymentResponse;
import com.example.sms.entity.Payment;
import com.example.sms.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ApiResponse<List<PaymentResponse>> list(@RequestParam(required = false) Long studentId) {
        return ApiResponse.success(paymentService.list(studentId));
    }

    @PostMapping
    public ApiResponse<PaymentResponse> create(@RequestParam Long studentId, @RequestBody Payment payment) {
        return ApiResponse.success(paymentService.create(payment, studentId));
    }

    @PutMapping("/{id}/pay")
    public ApiResponse<PaymentResponse> pay(@PathVariable Long id) {
        return ApiResponse.success(paymentService.pay(id));
    }
}
