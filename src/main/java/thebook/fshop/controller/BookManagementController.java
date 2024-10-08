package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookResponse;
import thebook.fshop.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookManagementController {
    BookService bookService;
    @GetMapping("/list-book")
    public ApiResponse<List<BookResponse>> viewListBook() {
        List<BookResponse> response = bookService.getListBook();
        return ApiResponse.<List<BookResponse>>builder().result(response).build();
    }
}
