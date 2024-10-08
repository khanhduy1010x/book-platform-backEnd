package thebook.fshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.BookRateRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookRateResponse;
import thebook.fshop.service.BookRateService;

import java.util.List;

@RestController
@RequestMapping("/api/book-rate")
@RequiredArgsConstructor
public class BookRateController {

    private final BookRateService bookRateService;

    // API để thêm đánh giá sách
    @PostMapping("/add")
    public ApiResponse<BookRateResponse> addBookRate(@RequestBody BookRateRequest request) {
        return ApiResponse.<BookRateResponse>builder()
                .result(bookRateService.addBookRate(request))
                .build();
    }

    // API để lấy danh sách đánh giá theo ID của sách
    @GetMapping("/book/{bookID}")
    public ApiResponse<List<BookRateResponse>> getBookRatesByBookID(@PathVariable int bookID) {
        return ApiResponse.<List<BookRateResponse>>builder()
                .result(bookRateService.getBookRatesByBookID(bookID))
                .build();
    }
}
