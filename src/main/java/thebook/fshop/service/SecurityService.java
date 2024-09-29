package thebook.fshop.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    AccountsRepository accountsRepository;

    public Account getAccountByJWT() {
        var context = SecurityContextHolder.getContext();
        var phone = context.getAuthentication().getName();
        log.info("So dien thoai la " + phone);
        return accountsRepository.findByPhone(phone).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
}
