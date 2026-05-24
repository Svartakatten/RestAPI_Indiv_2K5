package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class BookResponseDTO {
    @NotBlank(message = "Id is required")
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Isbn is required")
    private String isbn;
    @NotBlank(message = "authorId is required")
    private Long authorId;
    @NotBlank(message = "authorName is required")
    private String authorName;

    @NotBlank(message = "availability is required")
    private boolean available;


    // Gets
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Long getAuthorId() { return authorId; }
    public String getAuthorName() {return authorName; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return available; }


    // Sets
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setAvailable(boolean available) { this.available = available; }

}
