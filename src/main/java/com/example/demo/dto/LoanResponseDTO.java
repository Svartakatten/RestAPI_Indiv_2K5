package com.example.demo.dto;

import com.example.demo.entity.Loan;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class LoanResponseDTO {
    @NotBlank(message = "Id is required")
    private Long id;
    @NotBlank(message = "BookId is required")
    private Long bookId;
    @NotBlank(message = "BookTitle is required")
    private String bookTitle;
    @NotBlank(message = "LoanDate is required")
    private LocalDateTime loanDate;
    @NotBlank(message = "ReturnDate is required")
    private LocalDateTime returnDate;

    public LoanResponseDTO() {}

    public LoanResponseDTO(Loan loan) {
        this.id = loan.getId();

        if (loan.getBook() != null) {
            this.bookId = loan.getBook().getId();
            this.bookTitle = loan.getBook().getTitle();
        }

        this.loanDate = loan.getLoanDate();
        this.returnDate = loan.getReturnDate();
    }

    // Gets
    public Long getId() { return id; }

    public Long getBookId() { return bookId; }

    public String getBookTitle() { return bookTitle; }

    public LocalDateTime getLoanDate() { return loanDate; }

    public LocalDateTime getReturnDate() { return returnDate; }

    // Sets
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public void setId(Long id) { this.id = id; }

    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public void setLoanDate(LocalDateTime loanDate) { this.loanDate = loanDate; }
}
