package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthorRequestDTO;
import com.example.demo.dto.AuthorResponseDTO;
import com.example.demo.dto.BookResponseDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.exception.AuthorNotFoundException;
import com.example.demo.repository.AuthorRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorResponseDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("404: Author not found with the given id: " + id));
        return mapToResponse(author);
    }

    @Transactional
    public List<BookResponseDTO> getBooksByAuthorId(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("404: Author not found"));

        return author.getBooks().stream()
                .map(this::mapBookToResponse)
                .toList();
    }

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

