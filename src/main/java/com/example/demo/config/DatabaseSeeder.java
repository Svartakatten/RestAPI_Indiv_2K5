package com.example.demo.config;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner seedDatabase(AuthorRepository authorRepository, BookRepository bookRepository) {
        return args -> {
            if (authorRepository.count() == 0) {

                Author tolkien = new Author("J.R.R. Tolkien");
                Author orwell = new Author("George Orwell");

                Book hobbit = new Book("The Hobbit", tolkien, "978-0547928227", 1937);
                hobbit.setLibraryBranch("Central City Library");

                Book fellowship = new Book("The Fellowship of the Ring", tolkien, "978-0547928210", 1954);
                fellowship.setLibraryBranch("Central City Library");

                Book nineteenEightyFour = new Book("1984", orwell, "978-0451524935", 1949);
                nineteenEightyFour.setLibraryBranch("Westside Branch");

                tolkien.setBooks(List.of(hobbit, fellowship));
                orwell.setBooks(List.of(nineteenEightyFour));

                authorRepository.saveAll(List.of(tolkien, orwell));

                System.out.println("Database seeded: 2 Authors, 3 Books.");
            }
        };
    }
}
