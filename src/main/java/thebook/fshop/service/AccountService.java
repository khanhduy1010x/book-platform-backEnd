package thebook.fshop.service;

import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AccountCreationRequest;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.MemberType;
import thebook.fshop.helper.Role;
import thebook.fshop.mapper.AccountMapper;
import thebook.fshop.repository.AccountsRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountService {
    AccountsRepository accountsRepository;
    AccountMapper accountMapper;
    SecurityService securityService;
    private RedisTemplate<String, Object> template;

    public AccountResponse createAccount(AccountCreationRequest request) {
        String otp = (String) template.opsForValue().get(request.getPhone());
        if (otp == null) throw new AppException(ErrorCode.EXPIRED_OTP);
        log.info("IT IS: " + (Objects.equals(otp, request.getOtp())));
        log.info("Redis OTP  : " + otp);
        log.info("Request OTP: " + request.getOtp());
        if (!Objects.equals(otp, request.getOtp())) throw new AppException(ErrorCode.INVALID_OTP);
        if (accountsRepository.existsByPhone(request.getPhone())) throw new AppException(ErrorCode.EXITS_USERNAME);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Account account = accountMapper.toAccount(request);
        account.setRole(Role.USER);
        account.setMemberType(MemberType.NONE);
        account.setAmount(0L);
        return accountMapper.toAccountResponse(accountsRepository.save(account));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse getAccountByUserID(int ID) {
        return accountMapper.toAccountResponse(
                accountsRepository.findById(ID).orElseThrow(() -> new AppException(ErrorCode.NOT_EXITS_ACCOUNT)));
    }

    public AccountResponse getMyInfo() {
        var account = securityService.getAccountByJWT();
        return accountMapper.toAccountResponse(account);
    }
}
