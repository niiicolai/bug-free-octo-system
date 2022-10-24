package com.example.demo.model;

import java.time.LocalDateTime;

public class Wishlist {
    
    private long id;
    private String title;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wishlist() {
        
    }

    public Wishlist(long id, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }
}
