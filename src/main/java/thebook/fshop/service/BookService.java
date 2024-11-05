package thebook.fshop.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.FilterRequest;
import thebook.fshop.DTO.Response.BookCateResponse;
import thebook.fshop.DTO.Response.ListBookByCateResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Category;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.BookType;
import thebook.fshop.helper.MemberType;
import thebook.fshop.mapper.SearchBookMapper;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookService {
    BookRepository bookRepository;
    CategoryRepository categoryRepository;
    SearchBookMapper searchBookMapper;

    public List<ListBookByCateResponse> searchBook(String query) {
        // Fetching books from the repository
        log.info(query);
        List<Book> books = bookRepository.findByBookNameAndAuthorAndCategory(
                query.toLowerCase());
        if (books.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        Map<String, List<Book>> booksByCate=books.stream().collect(Collectors.groupingBy(book -> book.getCategory().getCateName()));
        List<ListBookByCateResponse> response = booksByCate.entrySet().stream()
                .map(entry -> new ListBookByCateResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return response;
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

    public Book getBookById(int  id) {
        return bookRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
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
                    .filter(book -> book.getAuthor().getName().contains(author))
                    .collect(Collectors.toList());
        }

        return books;
    }
    public List<Category> getCateIdsByBookType(String bookType) {
        return bookRepository.findCateIdsByBookType(BookType.valueOf(bookType));
    }
    public List<BookCateResponse> getByCateAndBookType(int cateID,String bookType) {
        List<Book> books = bookRepository.findBookByCategory_IDAndBookTypeOrderByMemberTypeDesc(cateID, BookType.valueOf(bookType));
        Comparator<MemberType> memberTypeComparator = Comparator.comparingInt(memberType -> {
            switch (memberType) {
                case PREMIUM: return 1;
                case ADVANCE: return 2;
                case BASIC: return 3;
                case NONE: return 4;
                default: throw new IllegalArgumentException("Unknown MemberType: " + memberType);
            }
        });

        books = books.stream()
                .sorted((b1, b2) -> memberTypeComparator.compare(b1.getMemberType(), b2.getMemberType()))
                .collect(Collectors.toList());

        Map<MemberType, List<Book>> booksByMemberType = books.stream()
                .collect(Collectors.groupingBy(Book::getMemberType));

        List<BookCateResponse> response = booksByMemberType.entrySet().stream()
                .map(entry -> new BookCateResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return response;
    }

}
