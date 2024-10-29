package thebook.fshop.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.DTO.Response.RecommendationResponse;
import thebook.fshop.service.BookReadHistoryService;

@RestController
@RequestMapping("/account/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookReadHistoryController {

    BookReadHistoryService bookReadHistoryService;

    @GetMapping("/history")
    public ApiResponse<List<BookReadHistoryResponse>> getBookReadHistory() {
        return ApiResponse.<List<BookReadHistoryResponse>>builder()
                .result(bookReadHistoryService.getBookReadHistoryByAccount())
                .build();
    }
    @GetMapping("/recommend")
    public ApiResponse<List<RecommendationResponse>> getBookRecommendation() {
        log.info("aaaaaaaaaaaa");
        return ApiResponse.<List<RecommendationResponse>>builder()
                .result(bookReadHistoryService.getBookRecommendation())
                .build();
    }
}
