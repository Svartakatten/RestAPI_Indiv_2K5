package com.example.demo.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookRequestDTO;
import com.example.demo.dto.BookResponseDTO;
import com.example.demo.dto.v2.BookRequestDTOV2;
import com.example.demo.dto.v2.BookResponseDTOV2;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.exception.AuthorNotFoundException;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;

import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public BookResponseDTOV2 createBookV2(BookRequestDTOV2 request) {
        Book book = new Book();
        book.setTitle(request.getTitle());

        Author author = authorRepo.findById(request.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + request.getAuthorId()));
        book.setAuthor(author);

        Book savedBook = bookRepo.save(book);

        // Sparar till det virtuella fältet i lokal cache för att inte påverka den körande databasen
        availabilityCache.put(savedBook.getId(), request.isAvailable());

        return convertToDtoV2(savedBook);
    }

    @Cacheable(value = "books", key = "#id")
    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepo.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book with id " + id + "missing"));

        return mapToResponse(book);
    }

    @Cacheable(value = "books", key = "#id")
    @Transactional(readOnly = true)
    public BookResponseDTOV2 getBookByIdV2(Long id) {
        Book book = bookRepo.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book with id " + id + "missing"));

        return convertToDtoV2(book);
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepo.findAll(pageable);

        return books.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTOV2> getAllBooksV2(Pageable pageable) {
        Page<Book> bookPage = bookRepo.findAll(pageable);
        return bookPage.map(this::convertToDtoV2);
    }

    @Transactional
    @CacheEvict(value = "books", allEntries = true)
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
    @CacheEvict(value = "books", allEntries = true)
    public BookResponseDTOV2 updateBookV2(Long id, BookRequestDTOV2 request) {
    Book book = bookRepo.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));

        book.setTitle(request.getTitle());

        Author author = authorRepo.findById(request.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + request.getAuthorId()));
        book.setAuthor(author);

        availabilityCache.put(id, request.isAvailable());

        return convertToDtoV2(book);
    }

    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public void removeBook(Long id) {
        Book book = bookRepo.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " missing"));

        bookRepo.delete(book);
    }

    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public void removeBookV2(Long id) {
        Book book = bookRepo.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " missing"));

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

    private BookResponseDTOV2 convertToDtoV2(Book book) {
        BookResponseDTOV2 dto = new BookResponseDTOV2();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        
        dto.setLibraryBranch(book.getLibraryBranch()); 
        
        return dto;
    }

}
