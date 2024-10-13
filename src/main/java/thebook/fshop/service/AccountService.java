package thebook.fshop.service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AccountCreationRequest;
import thebook.fshop.DTO.Request.UpdateAccountInformationRequest;
import thebook.fshop.DTO.Request.UpdateAvatarRequest;
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
    RedisTemplate<String, Object> template;

    @NonFinal
    @Value("${upload.path}")
    String UPLOAD_PATH;

    @NonFinal
    @Value("${path.avatar}")
    String PATH_AVATAR;

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

    public void updateAvatar(UpdateAvatarRequest request) {
        // Retrieve the account associated with the currently authenticated user
        var account = securityService.getAccountByJWT();
        MultipartFile fileAvatar = request.getFile();
        if (fileAvatar != null && !fileAvatar.isEmpty()) {
            String uniqueID = UUID.randomUUID().toString();
            String fileName = fileAvatar.getOriginalFilename() + uniqueID;
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String filePart = UPLOAD_PATH + File.separator;
            log.info(filePart);
            try {
                fileAvatar.transferTo(new File(filePart, fileName));
                account.setAvatar(PATH_AVATAR + fileName);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new AppException(ErrorCode.INVALID_FILE_NULL);
            }
        }
        accountsRepository.save(account);
    }

    public void updateInformation(UpdateAccountInformationRequest request) {
        var account = securityService.getAccountByJWT();

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_NAME_NULL);
        }
        if (!request.getName().matches("^[A-Za-zÀ-ỹ\\s]+$")) {
            throw new AppException(ErrorCode.INVALID_NAME);
        }

        if (request.getBirth() == null || request.getBirth().toString().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_BIRTH);
        }
        account.setFullName(request.getName());
        account.setBirth(request.getBirth());
        log.info(request.getName());
        log.info(request.getBirth().toString());
        accountsRepository.save(account);
    }
}
