package thebook.fshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import thebook.fshop.DTO.Request.*;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.DTO.Response.ForgotPasswordResponse;
import thebook.fshop.DTO.Response.ListAccountResponse;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.LoginType;
import thebook.fshop.helper.MemberType;
import thebook.fshop.helper.Role;
import thebook.fshop.mapper.AccountMapper;
import thebook.fshop.repository.AccountsRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // Fetch all users with ADMIN access
    @PreAuthorize("hasRole('ADMIN')")
    public List<ListAccountResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountsPage = accountsRepository.findAll(pageable);
        return accountsPage.getContent().stream()
                .map(accountMapper::toListAccountResponse)
                .collect(Collectors.toList());
    }

    // Create a new account
    public AccountResponse createAccount(AccountCreationRequest request) {
        String otp = (String) template.opsForValue().get(request.getPhone());
        if (otp == null) throw new AppException(ErrorCode.EXPIRED_OTP);
        if (!Objects.equals(otp, request.getOtp())) throw new AppException(ErrorCode.INVALID_OTP);
        if (accountsRepository.existsByPhone(request.getPhone())) throw new AppException(ErrorCode.EXITS_USERNAME);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Account account = accountMapper.toAccount(request);
        account.setRole(Role.USER);
        account.setMemberType(MemberType.NONE);
        account.setAmount(0L);
        account.setLoginType(LoginType.NORMAL);

        return accountMapper.toAccountResponse(accountsRepository.save(account));
    }

    // Fetch a specific account by its ID with ADMIN access
    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse getAccountByUserID(int ID) {
        return accountMapper.toAccountResponse(
                accountsRepository.findById(ID)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT)));
    }

    // Fetch the account info of the current authenticated user
    public AccountResponse getMyInfo() {
        var account = securityService.getAccountByJWT();
        AccountResponse accountResponse = accountMapper.toAccountResponse(account);
        accountResponse.setHasPassword(account.getPassword() != null);
        return accountResponse;
    }

    // Update the user's avatar
    public void updateAvatar(UpdateAvatarRequest request) {
        var account = securityService.getAccountByJWT();
        MultipartFile fileAvatar = request.getFile();

        if (fileAvatar != null && !fileAvatar.isEmpty()) {
            String uniqueID = UUID.randomUUID().toString();
            String fileName = fileAvatar.getOriginalFilename() + uniqueID;
            File uploadDir = new File(UPLOAD_PATH);

            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new AppException(ErrorCode.INVALID_FILE_NULL);
            }

            String filePart = UPLOAD_PATH + File.separator;
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

    // Update account information
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

        accountsRepository.save(account);
    }

    // Set or skip password prompt
    public void setPasswordPrompt(SetPasswordPromptRequest request) {
        var account = securityService.getAccountByJWT();
        account.setSkip_password_prompt(request.isSkip());
        accountsRepository.save(account);
    }

    // Promote or change a user's role (ADMIN access)
    @PreAuthorize("hasRole('ADMIN')")
    public void upRole(MemberRoleUpRequest request) {
        Account account = accountsRepository.findById(request.getAccID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        account.setRole(Role.valueOf(request.getRole()));
        accountsRepository.save(account);
    }

    // Ban an account (ADMIN access)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> banAccount(int accountId) {
        var currentAdmin = securityService.getAccountByJWT();
        if (currentAdmin.getAccID() == accountId) {
            throw new AppException(ErrorCode.CANNOT_BAN_OWN_ACCOUNT);
        }
        Account account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT));
        if (account.isBanned()) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_BANNED);
        }
        account.setBanned(true);
        accountsRepository.save(account);
        return ApiResponse.<Void>builder().build();
    }

    // Unlock a banned account (ADMIN access)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> unlockAccount(int accountId) {
        Account account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT));
        if (!account.isBanned()) {
            return ApiResponse.<Void>builder().build();
        }
        account.setBanned(false);
        accountsRepository.save(account);
        return ApiResponse.<Void>builder().build();
    }

    // Get email or phone by username for password recovery
    public ForgotPasswordResponse getEmailPhoneByUserName(ForgotPasswordRequest request) {
        return accountMapper.toForgotPasswordResponse(
                accountsRepository.findByUsername(request.getUsername())
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT)));
    }

    // Fetch users by MemberType
    public List<ListAccountResponse> getUserByType(MemberType memberType) {
        // Fetch all accounts from the repository
        List<Account> accounts = accountsRepository.findAll();

        // Filter the accounts by MemberType
        List<Account> filteredAccounts = accounts.stream()
                .filter(account -> account.getMemberType() == memberType)
                .collect(Collectors.toList());

        // Map the filtered accounts to ListAccountResponse
        return filteredAccounts.stream()
                .map(accountMapper::toListAccountResponse)
                .collect(Collectors.toList());
    }
}
