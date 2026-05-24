package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Author ID is required")
    private Long authorId;
    @NotBlank(message = "Isbn is required")
    private String isbn;

    @NotBlank(message = "availability is required")
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
