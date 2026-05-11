package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookRequestDTO;
import com.example.demo.dto.BookResponseDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.exception.AuthorNotFoundException;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;

import jakarta.transaction.Transactional;

@Service
public class BookService {
    
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    private final Map<Long, Boolean> availabilityCache = new ConcurrentHashMap<>();

    public BookService(BookRepository bookRepo, AuthorRepository authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    @Transactional
    public BookResponseDTO createBook(BookRequestDTO request) {
        Book book = new Book();
        book.setTitle(request.getTitle());

        Author author = authorRepo.findById(request.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + request.getAuthorId()));
        book.setAuthor(author);

        Book savedBook = bookRepo.save(book);

        // Sparar till det virtuella fältet i lokal cache för att inte påverka den körande databasen
        availabilityCache.put(savedBook.getId(), request.isAvailable());

        return mapToResponse(savedBook);
    }

    public BookResponseDTO getBookByIsbn(String isbn) {
        Book book = bookRepo.findByIsbn(isbn)
            .orElseThrow(() -> new BookNotFoundException("Book with isbn " + isbn + "missing"));
        
        return mapToResponse(book);
    }

    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = bookRepo.findAll();

        return books.stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Transactional
    public BookResponseDTO updateBook(Long id, BookRequestDTO request) {
    Book book = bookRepo.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));

        book.setTitle(request.getTitle());

        Author author = authorRepo.findById(request.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + request.getAuthorId()));
        book.setAuthor(author);

        availabilityCache.put(id, request.isAvailable());

        return mapToResponse(book);
    }

    @Transactional
    public void removeBook(Long id) {
        Book book = bookRepo.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book with id " + id + "missing"));

        bookRepo.delete(book);
    }

    private BookResponseDTO mapToResponse(Book book) {
        BookResponseDTO response = new BookResponseDTO();
        response.setId(book.getId());
        response.setTitle(book.getTitle());

        if (book.getAuthor() != null) {
            response.setAuthorId(book.getAuthor().getId());
            response.setAuthorName(book.getAuthor().getName());
        }

        // Hämtar ett virtuellt värde från cachen, annars default'ar till false om det inte finns
        boolean isAvailable = availabilityCache.getOrDefault(book.getId(), false);
        response.setAvailable(isAvailable);

        return response;
    }

}
