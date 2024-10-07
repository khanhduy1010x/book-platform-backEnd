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
@RequestMapping("/api/book-history")
public class BookReadHistoryController {

    @Autowired
    private BookReadHistoryService bookReadHistoryService;

    // API để thêm sách vào lịch sử đã đọc
    @PostMapping("/add")
    public void addBookReadHistory(@RequestParam int accID, @RequestParam int bookID) {
        Account account = new Account();
        account.setAccID(accID); // Sử dụng đối tượng Account

        Book book = new Book();
        book.setID(bookID); // Sử dụng đối tượng Book

        bookReadHistoryService.addBookReadHistory(account, book);
    }

    // API để lấy lịch sử đọc sách của tài khoản
    @GetMapping("/account/history")
    public ApiResponse<List<BookReadHistoryResponse>> getBookReadHistory() {
        return ApiResponse.<List<BookReadHistoryResponse>>builder()
                .result(bookReadHistoryService.getBookReadHistoryByAccount())
                .build();
    }

    // API để lấy lịch sử đọc sách theo tài khoản và sách
    @GetMapping("/account/{accID}/book/{bookID}")
    public List<BookReadHistoryResponse> getBookReadHistoryByAccountAndBook(
            @PathVariable int accID, @PathVariable int bookID) {
        return bookReadHistoryService.getBookReadHistoryByAccountAndBook(accID, bookID);
    }
}
