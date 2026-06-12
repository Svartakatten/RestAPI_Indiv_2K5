package com.example.demo.service;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanService.class);
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public LoanResponseDTO createLoan(LoanRequestDTO request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> {
                    log.warn("Failed loan attempt: Book with id {} does not exist", request.getBookId());
                    return new ResourceNotFoundException("Book not found with id: " + request.getBookId());
                });

        if (book.isLoanedOut()) {
            log.warn("Loan attempt rejected: The book with ID {} ​​('{}') is already on loan", book.getId(), book.getTitle());
            throw new BookAlreadyLoanedException("Book is already loaned out");
        }

        book.setLoanedOut(true);
        bookRepository.saveAndFlush(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());

        Loan savedLoan = loanRepository.saveAndFlush(loan);

        log.info("Transaction approved: New loan registered. Loan ID: {}, Book ID: {}, Title: {}", 
                savedLoan.getId(), book.getId(), book.getTitle());

        return new LoanResponseDTO(savedLoan);
    }

    @Transactional(readOnly = true)
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

