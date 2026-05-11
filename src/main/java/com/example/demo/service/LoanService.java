package com.example.demo.service;

@Service
@Transactional
public class LoanServiceV2 {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanServiceV2(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public LoanResponseDTOV2 createLoan(LoanRequestDTOV2 request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + request.getBookId()));

        if (book.isLoanedOut()) {
            throw new BookAlreadyLoanedException("Book is already loaned out");
        }

        book.setLoanedOut(true);
        bookRepository.saveAndFlush(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());

        Loan savedLoan = loanRepository.saveAndFlush(loan);

        return new LoanResponseDTOV2(savedLoan);
    }

    public List<LoanResponseDTOV2> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private LoanResponseDTOV2 mapToResponse(Loan loan) {
        LoanResponseDTOV2 response = new LoanResponseDTOV2();
        response.setId(loan.getId());
        response.setBookId(loan.getBook().getId());
        response.setBookTitle(loan.getBook().getTitle());
        response.setLoanDate(loan.getLoanDate());
        response.setReturnDate(loan.getReturnDate());
        return response;
    }
}

