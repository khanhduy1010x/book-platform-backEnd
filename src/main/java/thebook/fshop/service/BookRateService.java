package thebook.fshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import thebook.fshop.DTO.Request.BookRateRequest;
import thebook.fshop.DTO.Response.BookRateResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookRate;
import thebook.fshop.mapper.BookRateMapper;
import thebook.fshop.repository.BookRateRepository;
import thebook.fshop.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookRateService {
    BookRateRepository bookRateRepository;
    BookRepository bookRepository;
    BookRateMapper bookRateMapper;

    // Thêm đánh giá sách
    public BookRateResponse addBookRate(BookRateRequest request) {
        // Lấy sách từ cơ sở dữ liệu
        Book book = bookRepository.findById(request.getBookID())
                .orElseThrow(() -> new RuntimeException("Sách không tồn tại"));

        // Tạo đối tượng BookRate mới
        BookRate bookRate = BookRate.builder()
                .book(book)
                .rate(request.getRate())
                .comment(request.getComment())
                .build();

        // Lưu vào cơ sở dữ liệu
        BookRate savedBookRate = bookRateRepository.save(bookRate);

        // Chuyển đổi và trả về DTO
        return bookRateMapper.toBookRateResponse(savedBookRate);
    }

    // Lấy danh sách đánh giá của sách theo ID
    public List<BookRateResponse> getBookRatesByBookID(int bookID) {
        return bookRateRepository.findByBook_ID(bookID).stream()
                .map(bookRateMapper::toBookRateResponse)
                .collect(Collectors.toList());
    }
}
