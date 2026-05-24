package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoanRequestDTO;
import com.example.demo.dto.LoanResponseDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Loan;
import com.example.demo.exception.BookAlreadyLoanedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public LoanResponseDTO createLoan(LoanRequestDTO request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + request.getBookId()));

        if (book.isLoanedOut()) {
            throw new BookAlreadyLoanedException("Book is already loaned out");
        }

        book.setLoanedOut(true);
        bookRepository.saveAndFlush(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());

        Loan savedLoan = loanRepository.saveAndFlush(loan);

        return new LoanResponseDTO(savedLoan);
    }

    public Page<LoanResponseDTO> getAllLoans(Pageable pageable) {
        return loanRepository.findAll(pageable).map(this::mapToResponse);
    }

    private LoanResponseDTO mapToResponse(Loan loan) {
        LoanResponseDTO response = new LoanResponseDTO();
        response.setId(loan.getId());
        response.setBookId(loan.getBook().getId());
        response.setBookTitle(loan.getBook().getTitle());
        response.setLoanDate(loan.getLoanDate());
        response.setReturnDate(loan.getReturnDate());
        return response;
    }
}

