package thebook.fshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.BookDetailRequest;
import thebook.fshop.DTO.Response.ListBookByCateResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Category;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.CategoryRepository;

import java.util.ArrayList;

import thebook.fshop.DTO.Request.SearchRequest;
import thebook.fshop.mapper.SearchBookMapper;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookService {
    BookRepository bookRepository;
    CategoryRepository categoryRepository;
    SearchBookMapper searchBookMapper;

    public List<Book> searchBook(SearchRequest query) {
        // Fetching books from the repository
        List<Book> books = bookRepository.findByBookNameAndAuthorAndMemberType(query.getQuery().toLowerCase());
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
        return bookRepository
                .findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }

}
