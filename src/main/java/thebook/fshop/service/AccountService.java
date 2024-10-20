package thebook.fshop.service;
import thebook.fshop.DTO.Response.ApiResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
import thebook.fshop.DTO.Request.*;
import thebook.fshop.DTO.Response.AccountResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.LoginType;
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

    // Method to get all users
    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getAllUsers() {
        return accountsRepository.findAll();
    }



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
        account.setLoginType(LoginType.NORMAL);
        return accountMapper.toAccountResponse(accountsRepository.save(account));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse getAccountByUserID(int ID) {
        return accountMapper.toAccountResponse(
                accountsRepository.findById(ID).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT)));
    }

    public AccountResponse getMyInfo() {
        var account = securityService.getAccountByJWT();
        AccountResponse accountResponse = accountMapper.toAccountResponse(account);
        accountResponse.setHasPassword(true);
        if(account.getPassword() == null) {
            accountResponse.setHasPassword(false);
        }
        return accountResponse;
    }

    public void updateAvatar(MultipartFile avatarFile) {
        // Retrieve the current account using JWT
        var account = securityService.getAccountByJWT();
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String fileName = avatarFile.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!fileExtension.equals("jpg") && !fileExtension.equals("png") && !fileExtension.equals("webp")) {
                throw new AppException(ErrorCode.INVALID_FILE_EXTENSION);
            }
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = uuid + "_" + fileName;
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String filePath = UPLOAD_PATH + File.separator + uniqueFileName;
            try {
                avatarFile.transferTo(new File(filePath));
                account.setAvatar(PATH_AVATAR + uniqueFileName);
            } catch (IOException e) {
                log.error("Error saving file: " + e.getMessage());
                throw new AppException(ErrorCode.INVALID_FILE_NULL_TYPE);
            }
            accountsRepository.save(account);
        } else {
            throw new AppException(ErrorCode.INVALID_FILE_NULL);
        }
    }


    public void updateInformation(UpdateAccountInformationRequest request) {
        var account = securityService.getAccountByJWT();
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            if (!request.getName().matches("^[A-Za-zÀ-ỹ\\s]+$")) {
                throw new AppException(ErrorCode.INVALID_NAME); // Invalid name format
            }
            account.setFullName(request.getName());
        }
        if (request.getBirth() != null && !request.getBirth().toString().trim().isEmpty()) {
            account.setBirth(request.getBirth());
        }
        accountsRepository.save(account);
    }


    public void setPasswordPrompt (SetPasswordPromptRequest request) {
        var account = securityService.getAccountByJWT();
        account.setSkip_password_prompt(request.isSkip());
        log.info("Is: {}", request.isSkip());
        accountsRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void upRole(MemberRoleUpRequest request) {
        int accID = request.getAccID();
        String newRole = request.getRole();
        Account account = accountsRepository.findById(accID)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        account.setRole(Role.valueOf(newRole));
        accountsRepository.save(account);
    }


    /**
     * Ban tài khoản
     * @param accountId ID của tài khoản cần ban
     * @return ApiResponse
     */
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> banAccount(int accountId) {
        // Lấy thông tin admin đang đăng nhập
        var currentAdmin = securityService.getAccountByJWT();

        // Kiểm tra nếu admin đang cố gắng tự khóa tài khoản của mình
        if (currentAdmin.getAccID() == accountId) {
            throw new AppException(ErrorCode.CANNOT_BAN_OWN_ACCOUNT);  // Trả về lỗi mới nếu admin tự khóa
        }

        // Tìm kiếm tài khoản theo ID
        Account account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT));

        // Kiểm tra nếu tài khoản đã bị khóa
        if (account.isBanned()) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_BANNED);  // Sử dụng mã lỗi 1017 nếu cần
        }

        // Tiến hành khóa tài khoản
        account.setBanned(true);
        accountsRepository.save(account);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Account banned successfully.")
                .build();
    }

    /**
     * Mở khóa tài khoản
     * @param accountId ID của tài khoản cần mở khóa
     * @return ApiResponse
     */
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> unlockAccount(int accountId) {
        Account account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT));

        if (!account.isBanned()) {
            return ApiResponse.<Void>builder()
                    .code(400)
                    .message("Account is not banned.")
                    .build();
        }

        account.setBanned(false);
        accountsRepository.save(account);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Account unlocked successfully.")
                .build();
    }
}
