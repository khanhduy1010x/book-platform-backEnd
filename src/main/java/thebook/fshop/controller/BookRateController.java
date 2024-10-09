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

    @PostMapping("/add")
    public ApiResponse<?> addBookRate(@RequestBody BookRateRequest request) {
        bookRateService.addBookRate(request);
        return ApiResponse.builder()
                .build();
    }

    @GetMapping("/book/{bookID}")
    public ApiResponse<List<BookRateResponse>> getBookRatesByBookID(@PathVariable int bookID) {
        return ApiResponse.<List<BookRateResponse>>builder()
                .result(bookRateService.getBookRatesByBookID(bookID))
                .build();
    }
}
