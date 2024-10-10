package thebook.fshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import thebook.fshop.DTO.Request.BookRateRequest;
import thebook.fshop.DTO.Response.BookRateResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookRate;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.mapper.BookRateMapper;
import thebook.fshop.repository.BookRateRepository;
import thebook.fshop.repository.BookRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookRateService {
    BookRateRepository bookRateRepository;
    BookRepository bookRepository;
    BookRateMapper bookRateMapper;
    SecurityService securityService;

    public void addBookRate(BookRateRequest request) {
        var account = securityService.getAccountByJWT();
        Book book =
                bookRepository.findById(request.getBookID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        BookRate bookRate = BookRate.builder()
                .book(book)
                .account(account)
                .rate(request.getRate())
                .comment(request.getComment())
                .build();
        bookRateRepository.save(bookRate);
    }

    public List<BookRateResponse> getBookRatesByBookID(int bookID) {
        return bookRateRepository.findByBook_ID(bookID).stream()
                .map(bookRateMapper::toBookRateResponse)
                .toList();
    }
}
