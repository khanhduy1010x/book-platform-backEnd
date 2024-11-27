package thebook.fshop.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddNewBookRequest;
import thebook.fshop.DTO.Request.BookDetailRequest;
import thebook.fshop.DTO.Request.BuyEbookRequest;
import thebook.fshop.DTO.Request.UpdateBookRequest;
import thebook.fshop.DTO.Response.*;
import thebook.fshop.entity.Author;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Category;
import thebook.fshop.entity.EbookShelf;
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
    public ApiResponse<BookDetailResponse> viewBookDetail(@PathVariable int id) {
        return ApiResponse.<BookDetailResponse>builder().result(bookService.getBookById(id)).build();
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
    @GetMapping("/get-by-type/{type}")
    public ApiResponse<List<ListBookByCateResponse>> getBookByType(@PathVariable String type) {
        var response = bookService.getListBookByType(type);
        return ApiResponse.<List<ListBookByCateResponse>>builder()
                .result(response)
                .build();
    }

    @PostMapping("/buy-ebook")
    public ApiResponse<?> buyBook(@RequestBody BuyEbookRequest request) {
        bookService.buyEbook(request);
        return ApiResponse.builder()
                .build();
    }
    @GetMapping("get-book-bought")
    public ApiResponse<List<EbookShelf>> getBookBought() {
                return ApiResponse.<List<EbookShelf>>builder()
                        .result(bookService.getBookBought())
                        .build();
    }

    @GetMapping("/get-all-book/{type}")
    public ApiResponse<ListBookAdminResponse> getAllBook(@PathVariable int type,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return   ApiResponse.<ListBookAdminResponse>builder()
                .result(bookService.getAllBookByType(type,page,size))
                .build();

    }

    @GetMapping("/get-all-cate")
    public ApiResponse<List<Category>> getAllCate() {
        return  ApiResponse.<List<Category>>builder()
                .result(bookService.getAllCate())
                .build();
    }
    @GetMapping("/get-all-author")
    public ApiResponse<List<Author>> getAllAuthor() {
        return  ApiResponse.<List<Author>>builder()
                .result(bookService.getAllAuthor())
                .build();
    }
    @PostMapping("/add-new-book")
    public ApiResponse<?> addNewBook(@ModelAttribute AddNewBookRequest request) {
        bookService.addNewBook(request);
        return  ApiResponse.builder()
                .build();

    }
    @PostMapping("/update-book")
    public ApiResponse<?> updateBook(@ModelAttribute UpdateBookRequest request) {
        bookService.updateBook(request);
        return  ApiResponse.builder()
                .build();

    }
    @GetMapping("/find-by-cate-and-bookType/{cate}/{bookType}")
    public ApiResponse<List<Book>> findBookByCateAndBookType(@PathVariable String cate, @PathVariable String bookType) {
        return ApiResponse.<List<Book>>builder()
                .result(bookService.findByCateAndBooKType(cate,bookType))
                .build();
    }



}
