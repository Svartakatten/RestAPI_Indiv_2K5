package com.example.demo.dto;

import com.example.demo.entity.Loan;

import java.time.LocalDateTime;

public class LoanResponseDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private LocalDateTime loanDate;
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
