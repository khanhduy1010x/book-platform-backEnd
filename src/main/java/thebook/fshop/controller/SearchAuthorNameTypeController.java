package thebook.fshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.SearchRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.ListBookByCateResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.service.BookService;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SearchAuthorNameTypeController {
    BookService bookService;

    @GetMapping("/search/{param}")
    ApiResponse<List<ListBookByCateResponse>>searchBook(@PathVariable String param) {
        log.info(param);
        return ApiResponse.<List<ListBookByCateResponse>>builder()
                .result(bookService.searchBook(param))
                .build();
    }
}
