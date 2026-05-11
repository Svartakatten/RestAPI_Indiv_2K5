package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Book {
    
    // Object
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 2, max = 100, message = "Title has to be between 2 and 100 symbols")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @Version
    private Integer version;

    private boolean isLoanedOut = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_loan_id")
    private Loan currentLoan;

    private String isbn;

    private int publishedYear;

    public Book() {}

    public Book(String title, Author author, String isbn, int publishedYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }

    // Gets
    public Long getId() { return id; }

    public String getTitle() { return title; }

    public Author getAuthor() { return author; }

    public String getIsbn() { return isbn; }

    public int getPublishedYear() { return publishedYear; }

    public boolean isLoanedOut() { return isLoanedOut; }

    public Loan getCurrentLoan() { return currentLoan; }

    public Integer getVersion() { return version; }




    // Sets
    public void setTitle(String title) { this.title = title; }

    public void setAuthor(Author author) { this.author = author; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public void setPublishedYear(int publishedYear) { this.publishedYear = publishedYear; }

    public void setLoanedOut(boolean loanedOut) { isLoanedOut = loanedOut; }

    public void setCurrentLoan(Loan currentLoan) { this.currentLoan = currentLoan; }

    public void setVersion(Integer version) { this.version = version; }

}
