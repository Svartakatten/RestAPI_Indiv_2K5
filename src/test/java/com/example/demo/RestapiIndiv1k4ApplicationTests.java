package com.example.demo;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.dto.AuthorRequestDTO;
import com.example.demo.dto.AuthorResponseDTO;
import com.example.demo.dto.BookRequestDTO;
import com.example.demo.dto.LoanRequestDTO;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class RestapiIndiv1k4ApplicationTests {

    @Autowired
    private RestTestClient restClient;

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    @AfterEach
    void tearDown() {
        loanRepository.deleteAllInBatch();
        bookRepository.findAll().forEach(b -> {
            b.setCurrentLoan(null);
            bookRepository.save(b);
        });
        bookRepository.deleteAllInBatch();
        authorRepo.deleteAllInBatch();
    }

    @Test
    void testCreateAuthorAndBookFlow() {
        AuthorRequestDTO authorReq = new AuthorRequestDTO();
        authorReq.setName("Astrid Lindgren");

        var authorResult = restClient.post().uri("/api/v1/authors")
                .body(authorReq)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AuthorResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(authorResult).isNotNull();
        Long authorId = authorResult.getId();

        BookRequestDTO bookReq = new BookRequestDTO();
        bookReq.setTitle("Pippi Långstrump");
        bookReq.setAuthorId(authorId);
        bookReq.setAvailable(true);

        restClient.post().uri("/api/v1/books")
                .body(bookReq)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.title").isEqualTo("Pippi Långstrump")
                .jsonPath("$.version").isEqualTo("v1");
    }

    @Test
    void testCreateLoanFlow() {
        Author author = authorRepo.save(new Author("J.R.R. Tolkien"));
        Book book = new Book("The Hobbit", author, "9780007458424", 1937);
        book.setLoanedOut(false);
        book = bookRepository.save(book);

        LoanRequestDTO loanReq = new LoanRequestDTO();
        loanReq.setBookId(book.getId());

        restClient.post().uri("/api/v1/loans")
                .body(loanReq)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.bookTitle").isEqualTo("The Hobbit");
    }

    @Test
    void testAlreadyLoanedBookReturns400() {
        Author author = authorRepo.save(new Author("George Orwell"));
        Book book = new Book("1984", author, "123456", 1949);
        book.setLoanedOut(false);
        book = bookRepository.save(book);

        LoanRequestDTO loanReq = new LoanRequestDTO();
        loanReq.setBookId(book.getId());

        restClient.post().uri("/api/v1/loans").body(loanReq).exchange().expectStatus().isCreated();

        restClient.post().uri("/api/v1/loans")
                .body(loanReq)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testGetNonExistentBookReturns404() {
        restClient.get().uri("/api/v1/books/9999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetNonExistentAuthorReturns404() {
        restClient.get().uri("/api/v1/authors/9999")
                .exchange()
                .expectStatus().isNotFound();
    }

    // Concurrency Testning
    @Test
    void testParallelLoanRequests() throws InterruptedException {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepo.deleteAll();

        var author = authorRepo.save(new Author("Concurrency Expert"));

        Book book = new Book();
        book.setTitle("Race Condition 101");
        book.setAuthor(author);
        book.setLoanedOut(false);
        book = bookRepository.saveAndFlush(book);

        Long bookId = book.getId();

        int numberOfThreads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        LoanRequestDTO loanReq = new LoanRequestDTO();
        loanReq.setBookId(bookId);

        for (int i = 0; i < numberOfThreads; i++) {
            executor.execute(() -> {
                try {
                    startLatch.await();
                    var response = restClient.post().uri("/api/v1/loans")
                            .body(loanReq)
                            .exchange()
                            .returnResult(String.class);

                    if (response.getStatus().is2xxSuccessful()) {
                        successCount.incrementAndGet();
                    } else {
                        if (failureCount.get() == 0) {
                            System.out.println("First failure status: " + response.getStatus());
                        }
                        failureCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        endLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        // Verifiering
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failureCount.get()).isEqualTo(numberOfThreads - 1);

        long finalLoanCount = loanRepository.countByBookId(bookId);
        assertThat(finalLoanCount).isEqualTo(1);
    }
}
