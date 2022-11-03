package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.repository.CustomImplementation.CrudRepository;

public class Wishlist {
    
    private long id;
    private long userId;
    private String title;
    private LocalDateTime createdAt;

    public Wishlist() {
    }

    public Wishlist(long id, long userId, String title, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.createdAt = createdAt;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean notAuthorizeNew() {
        return CustomUserDetails.AuthenticatedUser().getId() != userId;
    }

    public boolean notAuthorizeExisting(WishlistRepository repository) {
        Wishlist list = repository.findOne(id);
        long userId = CustomUserDetails.AuthenticatedUser().getId();
        return userId != list.getUserId() || userId != this.userId;
    }
}
