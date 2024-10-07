package thebook.fshop.controller;

import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.entity.BookReadHistory;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.service.BookReadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/book")
public class BookReadHistoryController {

    @Autowired
    private BookReadHistoryService bookReadHistoryService;

    @GetMapping("/account/history")
    public ApiResponse<List<BookReadHistoryResponse>> getBookReadHistory() {
        return ApiResponse.<List<BookReadHistoryResponse>>builder()
                .result(bookReadHistoryService.getBookReadHistoryByAccount())
                .build();
    }
}
