package com.example.demo.model;

import java.time.LocalDateTime;

public class Wishlist {
    
    private long id;
    private long userId;
    private String title;

    public Wishlist() {
    }

    public Wishlist(long id, long userId, String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
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

}
