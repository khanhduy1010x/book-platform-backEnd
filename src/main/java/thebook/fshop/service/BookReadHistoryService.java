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
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.DTO.Response.BookRecommendationResponse;
import thebook.fshop.DTO.Response.RecommendationResponse;
import thebook.fshop.entity.Book;
import thebook.fshop.mapper.BookReadHistoryMapper;
import thebook.fshop.repository.BooKReadHistoryRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookReadHistoryService {
    BookReadHistoryMapper bookReadHistoryMapper;
    BooKReadHistoryRepository booKReadHistoryRepository;
    SecurityService securityService;

    public List<BookReadHistoryResponse> getBookReadHistoryByAccount() {
        var account = securityService.getAccountByJWT();
        return booKReadHistoryRepository.findByAccount_AccID(account.getAccID()).stream()
                .map(bookReadHistoryMapper::toBookReadHistoryResponse)
                .toList();
    }

    public List<RecommendationResponse> getBookRecommendation() {
        var account = securityService.getAccountByJWT();

        // Retrieve the most-read author and category from the repository
        List<Object[]> mostReadAuthor = booKReadHistoryRepository.findMostReadAuthorByAccount(account.getAccID());
        List<Object[]> mostReadCategory = booKReadHistoryRepository.findMostReadCategoryByAccount(account.getAccID());

        List<RecommendationResponse> bookRecommendations = new ArrayList<>();

        // Check if there is a most-read author and category
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
                RecommendationResponse recommendation = new RecommendationResponse();
                recommendation.setBook(book);
                bookRecommendations.add(recommendation);
            }
        }

        return bookRecommendations;
    }
}
