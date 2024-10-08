package thebook.fshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import thebook.fshop.DTO.Response.BookResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookService {
    BookRepository bookRepository;
    public List<BookResponse> getListBook() {
        List<Book> books = bookRepository.findBooksWithCategory();

        // Mapping each Book entity to BookResponse
        return books.stream().map(book -> BookResponse.builder()
                .ID(book.getID())
                .category(book.getCategory())
                .bookName(book.getBookName())
                .author(book.getAuthor())
                .price(book.getPrice())
                .memberType(book.getMemberType())
                .url(book.getUrl())
                .coverImage(book.getCoverImage())
                .description(book.getDescription())
                .build()).collect(Collectors.toList());
    }
}
