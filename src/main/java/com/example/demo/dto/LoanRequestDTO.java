package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class LoanRequestDTO {
    @NotNull(message = "Book ID is required")
    private Long bookId;

    // Gets
    public Long getBookId() { return bookId; }

    // Sets
    public void setBookId(Long bookId) { this.bookId = bookId; }

}
