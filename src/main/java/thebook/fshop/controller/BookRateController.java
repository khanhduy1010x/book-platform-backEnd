package thebook.fshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import thebook.fshop.DTO.Request.BookRateRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookRateResponse;
import thebook.fshop.service.BookRateService;

@RestController
@RequestMapping("/api/book-rate")
@RequiredArgsConstructor
public class BookRateController {

    private final BookRateService bookRateService;

    // Thêm đánh giá sách
    @PostMapping("/add")
    public ApiResponse<?> addBookRate(@RequestBody BookRateRequest request) {
        bookRateService.addBookRate(request);
        return ApiResponse.builder().build();
    }

    // Lấy danh sách đánh giá sách theo bookID
    @GetMapping("/book/{bookID}")
    public ApiResponse<List<BookRateResponse>> getBookRatesByBookID(@PathVariable int bookID) {
        return ApiResponse.<List<BookRateResponse>>builder()
                .result(bookRateService.getBookRatesByBookID(bookID))
                .build();
    }

    // Ẩn đánh giá sách
    @PostMapping("/hide/{bookRateId}")
    public ApiResponse<?> hideBookRate(@PathVariable int bookRateId) {
        return bookRateService.hideBookRate(bookRateId);
    }
}
