package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class BookRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    private Long authorId;
    private String isbn;

    private boolean available;

    // Gets
    public String getTitle() { return title; }
    public Long getAuthorId() { return authorId; }
    public String getIsbn() { return isbn;}
    public boolean isAvailable() { return available; }


    // Sets
    public void setTitle(String title) { this.title = title; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setAvailable(boolean available) { this.available = available; }
}
