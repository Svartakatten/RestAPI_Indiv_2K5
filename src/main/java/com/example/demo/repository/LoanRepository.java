package com.example.demo.repository;

import com.example.demo.entity.Loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Long countByBookId(Long bookId);
}
