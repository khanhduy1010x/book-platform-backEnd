package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.CreateFlashSaleRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.service.FlashSaleService;

import java.util.List;

@RestController
@RequestMapping("/flashsale")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FlashSaleController {
FlashSaleService flashSaleService;

@PostMapping("/create")
    public ApiResponse<?> create(@RequestBody CreateFlashSaleRequest request) {
    flashSaleService.getAllFlashSales(request);


    return ApiResponse.builder().build();

}
    // API lấy danh sách tất cả các sản phẩm trong kho
    @GetMapping("/list")
    public ApiResponse<List<Book>> getAllBooks() {
        List<Book> bookListList = flashSaleService.getAllBooks();
        return ApiResponse.<List<Book>>builder()
                .result(bookListList)
                .build();
    }
}
