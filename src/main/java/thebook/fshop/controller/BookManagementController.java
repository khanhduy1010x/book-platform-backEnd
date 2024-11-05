package thebook.fshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.BookDetailRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookCateResponse;
import thebook.fshop.DTO.Response.ListBookByCateResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Category;
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

    @GetMapping("/book-detail/{id}")
    public ApiResponse<Book> viewBookDetail(@PathVariable int id) {
        return ApiResponse.<Book>builder().result(bookService.getBookById(id)).build();
    }

    @GetMapping("/filter-cate")
    public ApiResponse<List<Category>> getCateIds(@RequestParam String bookType) {
        return ApiResponse.<List<Category>>builder()
                .result( bookService.getCateIdsByBookType(bookType))
                .build();
    }
    @GetMapping("/get-by-cate-type")
    public ApiResponse<List<BookCateResponse>> getCateAndBookType(@RequestParam String bookType, @RequestParam int cateID) {
        return ApiResponse.<List<BookCateResponse>>builder()
                .result(bookService.getByCateAndBookType(cateID,bookType))
                .build();
    }
}
