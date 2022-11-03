package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.config.SecurityConfiguration;

public class User {
    
    private long id;
    private String fullname;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(long id, String fullname, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void encodePassword() {
		password = SecurityConfiguration.PASSWORD_ENCODER.encode(password);
    }

    public boolean notAuthorizeExisting() {
        return CustomUserDetails.AuthenticatedUser().getId() != id;
    }
}
