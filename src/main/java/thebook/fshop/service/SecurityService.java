package thebook.fshop.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.entity.Account;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.AccountsRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SecurityService {
    AccountsRepository accountsRepository;

    @PreAuthorize("isAuthenticated()")
    public Account getAccountByJWT() {
        var context = SecurityContextHolder.getContext();
        var username = context.getAuthentication().getName();
        return accountsRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
}
