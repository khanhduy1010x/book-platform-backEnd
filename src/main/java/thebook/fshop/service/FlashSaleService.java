package thebook.fshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thebook.fshop.DTO.Request.CreateFlashSaleRequest;
import thebook.fshop.DTO.Response.BookWithInventoryResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.FlashSale;
import thebook.fshop.entity.FlashSaleBook;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.FlashSaleBookRepository;
import thebook.fshop.repository.FlashSaleRepository;
import thebook.fshop.repository.WishListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FlashSaleService {

    FlashSaleRepository flashSaleRepository;
    SecurityService securityService;
    BookRepository bookRepository;
FlashSaleBookRepository flashSaleBookRepository;
    public void getAllFlashSales(CreateFlashSaleRequest request) {
   var  flashSale = FlashSale.builder()
           .startTime(request.getStartTime())
                   .endTime(request.getEndTime())
   .build();
var flashSaled =   flashSaleRepository.save(flashSale);
       var flashSaleBookArrayList = request.getBookSaleList().stream().map(bookSale ->
               FlashSaleBook.builder()
                .book(bookRepository.findById(bookSale.getBookId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND)))
               .flashSale(flashSaled)
                .salePrice(bookSale.getSalePrice())
                .quantity(bookSale.getQuantity())
               .isActive(!Objects.equals(bookSale.getIsActive(), "0"))
                .build()).toList();
        flashSaleBookRepository.saveAll(flashSaleBookArrayList);
    }

    public List<BookWithInventoryResponse> getAllBooksWithInventory() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(book -> {
            // Tìm số lượng trong kho của sách
            int quantityInStock = inventoryRepository.findByBook_ID(book.getBookID())
                    .map(Inventory::getQuantity)
                    .orElse(0); // Nếu không có, trả về 0

            // Tạo đối tượng DTO
            return BookWithInventoryResponse.builder()
                    .bookId(book.getBookID())
                    .bookName(book.getBookName())
                    .author(book.getAuthor())
                    .price(book.getPrice())
                    .quantityInStock(quantityInStock)
                    .build();
        }).collect(Collectors.toList());
    }
}
