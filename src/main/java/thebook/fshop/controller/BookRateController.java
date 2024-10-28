package thebook.fshop.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ApiResponse<Page<BookRateResponse>> getBookRatesByBookID(
            @PathVariable int bookID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookRateResponse> response = bookRateService.getBookRatesByBookID(bookID, pageable);
        return ApiResponse.<Page<BookRateResponse>>builder()
                .result(response)
                .build();
    }

    // Ẩn đánh giá sách
    @PostMapping("/hide/{bookRateId}")
    public ApiResponse<?> hideBookRate(@PathVariable int bookRateId) {
        return bookRateService.hideBookRate(bookRateId);
    }

}
