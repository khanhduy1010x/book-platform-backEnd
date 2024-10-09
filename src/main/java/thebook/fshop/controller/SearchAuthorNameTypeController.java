package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import thebook.fshop.DTO.Request.SearchRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BookResponse;

import thebook.fshop.entity.Book;
import thebook.fshop.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SearchAuthorNameTypeController {
    BookService bookService;

    @GetMapping("/search")
    ApiResponse<List<Book>> searchBook(@RequestBody SearchRequest query) {
log.info(query.toString());
        return ApiResponse.<List<Book>>builder().result(bookService.searchBook(query)).build();


    }
}
