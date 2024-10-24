package thebook.fshop.service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.BookDetailRequest;
import thebook.fshop.DTO.Request.FilterRequest;
import thebook.fshop.DTO.Request.SearchRequest;
import thebook.fshop.DTO.Response.ListBookByCateResponse;
import thebook.fshop.DTO.Response.ListBookMostStatistic;
import thebook.fshop.DTO.Response.ListReadBookStatisticResponse;
import thebook.fshop.DTO.Response.ListReaderStatisticResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookReadHistory;
import thebook.fshop.entity.Category;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.BookType;
import thebook.fshop.helper.EbookType;
import thebook.fshop.helper.MemberType;
import thebook.fshop.mapper.AccountMapper;
import thebook.fshop.mapper.SearchBookMapper;
import thebook.fshop.repository.BooKReadHistoryRepository;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.CategoryRepository;
import thebook.fshop.repository.OrderDetailRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookService {
    BookRepository bookRepository;
    CategoryRepository categoryRepository;
    SearchBookMapper searchBookMapper;
    OrderDetailRepository orderDetailRepository;
    BooKReadHistoryRepository booKReadHistoryRepository;
AccountMapper accountMapper;

    public List<Book> searchBook(SearchRequest query) {
        // Fetching books from the repository
        List<Book> books = bookRepository.findByBookNameAndAuthorAndMemberType(
                query.getQuery().toLowerCase());
        if (books.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return books;
    }

    public List<ListBookByCateResponse> getListBook() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) throw new AppException(ErrorCode.NOT_FOUND);
        List<ListBookByCateResponse> books = new ArrayList<>();
        for (Category category : categories) {
            var listBook = bookRepository.findBookByCategory_ID(category.getID());
            ListBookByCateResponse listBookResponse = ListBookByCateResponse.builder()
                    .cateName(category.getCateName())
                    .listBook(listBook)
                    .build();
            books.add(listBookResponse);
        }
        return books;
    }

    public Book getBookById(BookDetailRequest request) {
        return bookRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
    public List<Book> searchByFilter(FilterRequest query) {
        List<Book> books = bookRepository.findAll();
        if (query.getType() != null) {
            BookType bookType = BookType.valueOf(query.getType());
            books = books.stream()
                    .filter(book -> book.getBookType() == bookType)
                    .collect(Collectors.toList());
        }
        if (query.getAuthor() != null) {
            String author = query.getAuthor();
            books = books.stream()
                    .filter(book -> book.getAuthor().contains(author))
                    .collect(Collectors.toList());
        }

        return books;
    }

    public List<ListBookMostStatistic> getStatisticsOnMostPurchasedBooks() {
            // Fetch top 10 most purchased books using the repository method
            List<Book> topBooks = bookRepository.findTop10MostPurchasedBooks();
            // Map the books to ListBookMostStatistic objects
            return topBooks.stream()
                    .map(book -> {
                        // Fetch total quantity directly within this method
                        int totalQuantity = orderDetailRepository.findTotalQuantityByBookId(book.getID());
                        return ListBookMostStatistic.builder()
                                .book(book)
                                .total_quantity(totalQuantity)
                                .build();
                    })
                    .collect(Collectors.toList());
        }


    public List<ListReadBookStatisticResponse> getStatisticsOnMostReadBooks() {
        // Fetch top 10 most read books using the repository method
        List<Book> topBooks = booKReadHistoryRepository.findMostReadBooks();

        // Map the ListReadBookStatisticResponse objects with total read user count
        return topBooks.stream()
                .map(bookProj -> {
                    int totalQuantity = booKReadHistoryRepository.findTotalQuantityByBookId(bookProj.getID());
                   return ListReadBookStatisticResponse.builder()
                        .book(bookProj)
                           .total_read_user(totalQuantity)
                        .build();

                })
                .collect(Collectors.toList());
    }

        public List<ListReaderStatisticResponse> getStatisticsOnMostReader() {
            List<BookReadHistory> topBooks = booKReadHistoryRepository.findTop10Readers();
            return topBooks.stream()
                    .map(reader -> {
                        int total_read = booKReadHistoryRepository.findTotalBookByReader(reader.getId());
                        return ListReaderStatisticResponse.builder()
                                .acc(accountMapper.toAccountResponse(reader.getAccount()))
                                .total_read(total_read)
                                .build();

                    })
                    .collect(Collectors.toList());
        }
}




