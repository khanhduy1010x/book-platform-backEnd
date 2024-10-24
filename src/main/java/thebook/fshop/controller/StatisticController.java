package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thebook.fshop.DTO.Response.*;
import thebook.fshop.entity.Book;
import thebook.fshop.service.BookService;
import thebook.fshop.service.TransactionService;


import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StatisticController {
    BookService bookService;
    TransactionService transactionService;
    @GetMapping("/most-purchased-books")
    ApiResponse<List<ListBookMostStatistic>> viewStaticBookTop() {
        return ApiResponse.<List<ListBookMostStatistic>>builder().result(bookService.getStatisticsOnMostPurchasedBooks()).build();
    }
    @GetMapping("/most-read-books")
    ApiResponse<List<ListReadBookStatisticResponse>> viewStaticReadBook() {
        return ApiResponse.<List<ListReadBookStatisticResponse>>builder().result(bookService.getStatisticsOnMostReadBooks()).build();
    }
    @GetMapping("/most-reader")
    ApiResponse<List<ListReaderStatisticResponse>> viewStaticReader() {
        return ApiResponse.<List<ListReaderStatisticResponse>>builder().result(bookService.getStatisticsOnMostReader()).build();
    }
    @GetMapping("/pay-most-user")
    ApiResponse<List<ListStatisticPayMostResponse>> viewStaticPayMost() {
        return ApiResponse.<List<ListStatisticPayMostResponse>>builder().result(transactionService.getStatisticsPayMostReader()).build();
    }
    @GetMapping("/user-top-content")
    ApiResponse<List<ListStatisticTopContentResponse>> viewStaticTopContent() {
        return ApiResponse.<List<ListStatisticTopContentResponse>>builder().result(bookService.getStatisticsTopContent()).build();
    }

}
