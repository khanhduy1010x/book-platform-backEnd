package thebook.fshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.BookDetailRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.ListBookByCateResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.service.BookService;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookManagementController {
    BookService bookService;

    @GetMapping("/list-book")
    public ApiResponse<List<ListBookByCateResponse>> viewListBook() {
        var response = bookService.getListBook();
        return ApiResponse.<List<ListBookByCateResponse>>builder()
                .result(response)
                .build();
    }

    @GetMapping("/book-detail")
    public ApiResponse<Book> viewBookDetail(@RequestBody BookDetailRequest book) {
        return ApiResponse.<Book>builder().result(bookService.getBookById(book)).build();
    }
}
