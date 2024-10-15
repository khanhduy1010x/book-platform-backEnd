package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thebook.fshop.DTO.Request.SearchRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.service.AccountService;


import java.util.List;

@RestController
@RequestMapping("/SearchAccount")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SearchAccountController {
    AccountService accountService;

    @GetMapping("/search")
    ApiResponse<List<Account>> searchAccountNamePhoneEmail(@RequestBody SearchRequest query) {
        log.info(query.toString());
        return ApiResponse.<List<Account>>builder()
                .result(accountService.searchAccount(query))
                .build();
    }
}
