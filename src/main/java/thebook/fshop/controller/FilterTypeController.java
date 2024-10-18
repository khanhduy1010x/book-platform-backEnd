package thebook.fshop.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thebook.fshop.DTO.Request.FilterRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FilterTypeController {
    BookService bookService;

    @GetMapping("/search-type")
    public ApiResponse<List<Book>> searchBookByType(@RequestBody @Valid FilterRequest query) {
        List<Book> books = bookService.searchByFilter(query);
        return ApiResponse.<List<Book>>builder()
                .result(books)
                .message("Books fetched successfully")
                .build();
    }
}
