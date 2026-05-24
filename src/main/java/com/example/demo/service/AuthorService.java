package com.example.demo.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthorRequestDTO;
import com.example.demo.dto.AuthorResponseDTO;
import com.example.demo.dto.BookResponseDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.exception.AuthorNotFoundException;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "authors", key = "#author.id")
    public AuthorResponseDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("404: Author not found with the given id: " + id));
        return mapToResponse(author);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "author-books", key = "#id + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<BookResponseDTO> getBooksByAuthorId(Long id, Pageable pageable) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException("404: Author not found");
        }
        
        Page<Book> books = bookRepository.findByAuthorId(id, pageable);

        return books.map(this::mapBookToResponse);
    }

    @Transactional
    public AuthorResponseDTO createAuthor(AuthorRequestDTO request) {
        Author author = new Author();
        author.setName(request.getName());

        Author savedAuthor = authorRepository.save(author);

        return mapToResponse(savedAuthor);
    }

    private AuthorResponseDTO mapToResponse(Author author) {
        AuthorResponseDTO response = new AuthorResponseDTO();
        response.setId(author.getId());
        response.setName(author.getName());
        return response;
    }

    private BookResponseDTO mapBookToResponse(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        return dto;
    }
}

