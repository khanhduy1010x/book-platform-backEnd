package thebook.fshop.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.service.BookReadHistoryService;

@RestController
@RequestMapping("/account/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookReadHistoryController {

    BookReadHistoryService bookReadHistoryService;

    @GetMapping("/history")
    public ApiResponse<Page<BookReadHistoryResponse>> getBookReadHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookReadHistoryResponse> response = bookReadHistoryService.getBookReadHistoryByAccount(pageable);
        return ApiResponse.<Page<BookReadHistoryResponse>>builder()
                .result(response)
                .build();
    }
}
