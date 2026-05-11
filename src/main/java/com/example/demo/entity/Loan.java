package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Loan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Column(nullable = false, updatable = false)
    private LocalDateTime loanDate;

    private LocalDateTime returnDate;

    @PrePersist
    protected void onCreate() {
        this.loanDate = LocalDateTime.now();
    }

    // Gets

    public Long getId() { return id; }

    public Book getBook() { return book; }

    public LocalDateTime getLoanDate() { return loanDate; }

    public LocalDateTime getReturnDate() { return returnDate; }

    // Sets

    public void setId(Long id) { this.id = id;}

    public void setBook(Book book) { this.book = book; }

    public void setLoanDate(LocalDateTime loadDate) { this.loanDate = loadDate; }

    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

}
