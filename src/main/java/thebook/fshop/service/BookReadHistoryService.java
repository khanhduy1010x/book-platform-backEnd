package thebook.fshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
import thebook.fshop.entity.BookReadHistory;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.mapper.BookReadHistoryMapper;
import thebook.fshop.repository.BooKReadHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookReadHistoryService {
BookReadHistoryMapper bookReadHistoryMapper;
     BooKReadHistoryRepository booKReadHistoryRepository;
    SecurityService securityService;


    // Thêm lịch sử đọc sách
    public void addBookReadHistory(Account account, Book book) {
        BookReadHistory history = BookReadHistory.builder()
                .account(account)
                .book(book)
                .build();
        booKReadHistoryRepository.save(history);
    }

    // Lấy danh sách lịch sử đọc sách theo tài khoản
    public List<BookReadHistoryResponse> getBookReadHistoryByAccount() {
        var account = securityService.getAccountByJWT();
        return booKReadHistoryRepository.findByAccount_AccID(account.getAccID()).stream().map(bookReadHistoryMapper::toBookReadHistoryResponse).toList();
    }

    // Lấy lịch sử đọc sách theo tài khoản và sách
    public List<BookReadHistoryResponse> getBookReadHistoryByAccountAndBook(int accID, int ID) {
        return booKReadHistoryRepository.findByAccount_AccIDAndBook_ID(accID, ID).stream().map(bookReadHistoryMapper::toBookReadHistoryResponse).toList();
    }
}
