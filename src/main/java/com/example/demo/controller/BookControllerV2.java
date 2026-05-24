package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.v2.BookRequestDTOV2;
import com.example.demo.dto.v2.BookResponseDTOV2;
import com.example.demo.service.BookService;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("api/v2/books")
public class BookControllerV2 {
    
    private final BookService bookService;

    public BookControllerV2 (BookService bookService) { this.bookService = bookService;}

    // Build API
    @GetMapping
    public ResponseEntity<Page<BookResponseDTOV2>> getAllBooks(@ParameterObject @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BookResponseDTOV2> books = bookService.getAllBooksV2(pageable);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTOV2> getBookById(@PathVariable Long id) {
        BookResponseDTOV2 response = bookService.getBookByIdV2(id);

        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping
    public ResponseEntity<BookResponseDTOV2> createBook(@RequestBody BookRequestDTOV2 request) {
        BookResponseDTOV2 savedBook = bookService.createBookV2(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTOV2> updateBook(@PathVariable Long id, @RequestBody BookRequestDTOV2 request) {
        BookResponseDTOV2 savedBook = bookService.updateBookV2(id, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.removeBook(id);

        return ResponseEntity.noContent().build();
    }
    

}
