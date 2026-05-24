package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BookRequestDTO;
import com.example.demo.dto.BookResponseDTO;
import com.example.demo.service.BookService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/v1/books")
public class BookController {
    
    private final BookService bookService;

    public BookController (BookService bookService) { this.bookService = bookService;}

    // Build API
    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> getAllBooks(@PageableDefault(size = 20) Pageable pageable) {
        Page<BookResponseDTO> books = bookService.getAllBooks(pageable);

        return ResponseEntity.ok(books);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> getBookById(@PathVariable Long id, @PageableDefault(size = 20) Pageable pageable) {
        BookResponseDTO response = bookService.getBookById(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    

    @GetMapping
    public ResponseEntity<BookResponseDTO> getBookByIsbn(@PathVariable String isbn) {
        BookResponseDTO response = bookService.getBookByIsbn(isbn);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO request) {
        BookResponseDTO savedBook = bookService.createBook(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @RequestBody BookRequestDTO request) {
        BookResponseDTO savedBook = bookService.updateBook(id, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.removeBook(id);
    }
    

}
