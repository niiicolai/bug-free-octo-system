package com.example.demo.model;

import java.time.LocalDateTime;

public class Wish {
    
    private long id;
    private long wishlistId;
    private String content;

    public Wish() {
    }

    public Wish(long id, long wishlistId, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return this.id;
    }

    public void setWishlistId(long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public long getWishlistId() {
        return this.wishlistId;
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
}
