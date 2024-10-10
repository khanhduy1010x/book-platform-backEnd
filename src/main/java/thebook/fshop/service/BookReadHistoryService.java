package thebook.fshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.BookReadHistoryResponse;
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
}
