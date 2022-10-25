package com.example.demo.model;

import java.time.LocalDateTime;

public class Wish {
    
    private long id;
    private String content;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wish() {
        
    }

    public Wish(long id, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }
}
