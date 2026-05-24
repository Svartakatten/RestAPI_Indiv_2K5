package com.example.demo.dto.v2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookRequestDTOV2 {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Author ID is required")
    private Long authorId;
    @NotBlank(message = "Isbn is required")
    private String isbn;

    @NotBlank(message = "availability is required")
    private boolean available;

    private String libraryBranch;

    // Gets
    public String getLibraryBranch() { return libraryBranch; }
    public String getTitle() { return title; }
    public Long getAuthorId() { return authorId; }
    public String getIsbn() { return isbn;}
    public boolean isAvailable() { return available; }


    // Sets
    public void setLibraryBranch(String libraryBranch){ this.libraryBranch = libraryBranch; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setAvailable(boolean available) { this.available = available; }
}
