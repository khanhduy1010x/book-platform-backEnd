package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.ListBookMostStatistic;
import thebook.fshop.entity.Book;
import thebook.fshop.service.BookService;


import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StatisticController {
    BookService bookService;
    @GetMapping("/most-purchased-books")
    ApiResponse<List<ListBookMostStatistic>> viewStaticBookTop() {
        return ApiResponse.<List<ListBookMostStatistic>>builder().result(bookService.getStatisticsOnMostPurchasedBooks()).build();
    }
}
