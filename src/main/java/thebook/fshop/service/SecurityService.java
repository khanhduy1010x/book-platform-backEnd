package thebook.fshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
        if(phone !=null) return accountsRepository.findByPhone(phone).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        Authentication authentication = context.getAuthentication();
        if (authentication == null) throw new AppException(ErrorCode.UNAUTHORIZED);
        var principal = authentication.getPrincipal();
        if( principal instanceof Jwt jwt ) {
            String email = jwt.getClaim("email");
            return accountsRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        }
        throw new AppException(ErrorCode.NOT_FOUND);
    }
}
