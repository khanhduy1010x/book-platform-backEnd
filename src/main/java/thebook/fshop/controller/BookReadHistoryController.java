package thebook.fshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import thebook.fshop.DTO.Request.AddBookReadHistory;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.DTO.Response.RecommendationResponse;
import thebook.fshop.entity.BookReadHistory;
import thebook.fshop.service.BookReadHistoryService;

@RestController
@RequestMapping("/account/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookReadHistoryController {

    BookReadHistoryService bookReadHistoryService;

    @GetMapping("/history")
    public ApiResponse<List<BookReadHistoryResponse>> getBookReadHistory() {
        return ApiResponse.<List<BookReadHistoryResponse>>builder()
                .result(bookReadHistoryService.getBookReadHistoryByAccount())
                .build();
    }

    @PostMapping("/add-history")
    public ApiResponse<?> addBookReadHistory(@RequestBody AddBookReadHistory request) {
    bookReadHistoryService.addNewBookReadHistory(request);
        return ApiResponse.builder()
                .build();
    }
    @GetMapping("/recommend")
    public ApiResponse<RecommendationResponse> getBookRecommendation() {
        return ApiResponse.<RecommendationResponse>builder()
                .result(bookReadHistoryService.getBookRecommendation())
                .build();
    }
}
