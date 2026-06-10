package com.example.sms.dto;

public class LoginResponse {
    private Long id;
    private String username;
    private String role;
    private String realName;
    private Long studentId;

    public LoginResponse(Long id, String username, String role, String realName, Long studentId) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.realName = realName;
        this.studentId = studentId;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getRealName() {
        return realName;
    }

    public Long getStudentId() {
        return studentId;
    }
}
