package thebook.fshop.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddBookReadHistory;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.DTO.Response.RecommendationResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.BookReadHistory;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.mapper.BookReadHistoryMapper;
import thebook.fshop.repository.BooKReadHistoryRepository;
import thebook.fshop.repository.BookRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookReadHistoryService {
    BooKReadHistoryRepository booKReadHistoryRepository;
    SecurityService securityService;
    BookRepository bookRepository;


    public List<BookReadHistoryResponse> getBookReadHistoryByAccount() {
        var account = securityService.getAccountByJWT();
        return booKReadHistoryRepository.findByAccountId(account.getAccID());
    }

    public void addNewBookReadHistory(AddBookReadHistory request) {
        var book = bookRepository.findById(request.getBookID()).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        var account = securityService.getAccountByJWT();
        BookReadHistory bookReadHistory = booKReadHistoryRepository.findByAccount_AccIDAndBook_ID(account.getAccID(),book.getID());
        if (bookReadHistory == null) {
            bookReadHistory = BookReadHistory.builder().book(book).account(account).build();
        }
        bookReadHistory.setReadPercent(request.getPercent());
        booKReadHistoryRepository.save(bookReadHistory);


    }
    public RecommendationResponse getBookRecommendation() {
        var account = securityService.getAccountByJWT();

        // Retrieve the most-read author and category from the repository
        List<Object[]> mostReadAuthor = booKReadHistoryRepository.findMostReadAuthorByAccount(account.getAccID());
        List<Object[]> mostReadCategory = booKReadHistoryRepository.findMostReadCategoryByAccount(account.getAccID());


        // Check if there is a most-read author and category
        List<Book> listBook = new ArrayList<>();
        if (!mostReadAuthor.isEmpty() && !mostReadCategory.isEmpty()) {
            String author = (String) mostReadAuthor.get(0)[0]; // Extracting the most-read author
            String category = (String) mostReadCategory.get(0)[0]; // Extracting the most-read category

            // Find books by the most-read author
            List<Book> booksByAuthor = booKReadHistoryRepository.findBooksByAuthor(author);

            // Find books by the most-read category
            List<Book> booksByCategory = booKReadHistoryRepository.findBooksByCategory(category);

            // Combine the results, ensuring uniqueness
            Set<Book> uniqueBooks = new HashSet<>(booksByAuthor);
            uniqueBooks.addAll(booksByCategory);
            // Create RecommendationResponse objects for each unique book
            for (Book book : uniqueBooks) {
                if(book.getIsVisible()){
                    listBook.add(book);
                }
            }
        }

        return RecommendationResponse.builder()
                .listBook(listBook)
                .build();
    }

}
