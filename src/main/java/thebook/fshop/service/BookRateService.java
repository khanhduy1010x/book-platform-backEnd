package thebook.fshop.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import thebook.fshop.DTO.Request.BookRateRequest;
import thebook.fshop.DTO.Request.BookRateUpdateRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookRateResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookRate;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.Rate;
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

    // Thêm đánh giá sách
    public void addBookRate(BookRateRequest request) {
        var account = securityService.getAccountByJWT();
        Book book =
                bookRepository.findById(request.getBookID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        BookRate bookRate = BookRate.builder()
                .book(book)
                .account(account)
                .rate(Rate.values()[request.getRate()-1])
                .comment(request.getComment())
                .date(new Date())
                .build();
        bookRateRepository.save(bookRate);
    }
    public void updateBookRate(BookRateUpdateRequest request) {
        var bookRate = bookRateRepository.findById(request.getBookRateID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        bookRate.setRate(Rate.values()[request.getRate()-1]);
        bookRate.setComment(request.getComment());
        bookRateRepository.save(bookRate);
    }

    // Lấy danh sách đánh giá sách theo bookID
    public List<BookRateResponse> getBookRatesByBookID(int bookID) {
        return bookRateRepository.findByBook_ID(bookID).stream()
                .map(bookRateMapper::toBookRateResponse)
                .toList();
    }

    // Ẩn đánh giá sách
    public ApiResponse<Void> hideBookRate(int bookRateId) {
        Optional<BookRate> bookRateOpt = bookRateRepository.findById(bookRateId);
        if (bookRateOpt.isPresent()) {
            BookRate bookRate = bookRateOpt.get();
            if (!bookRate.isHidden()) {
                bookRate.setHidden(true);
                bookRateRepository.save(bookRate);
                return ApiResponse.<Void>builder()
                        .code(200)
                        .message("Book rate has been hidden successfully.")
                        .result(null)
                        .build();
            } else {
                return ApiResponse.<Void>builder()
                        .code(400)
                        .message("Book rate is already hidden.")
                        .result(null)
                        .build();
            }
        } else {
            return ApiResponse.<Void>builder()
                    .code(404)
                    .message("Book rate not found.")
                    .result(null)
                    .build();
        }
    }
}
