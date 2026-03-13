package thebook.fshop.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import thebook.fshop.DTO.Response.*;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.CountAccountByRole;
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
        if (account.getPassword() == null) {
            accountResponse.setHasPassword(false);
        }
        return accountResponse;
    }

    public String updateAvatar(UpdateAvatarRequest request) {
        var account = securityService.getAccountByJWT();
        MultipartFile fileAvatar = request.getFile();
        if (fileAvatar != null && !fileAvatar.isEmpty()) {
            String originalFileName = fileAvatar.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            String uniqueID = UUID.randomUUID().toString();
            String fileName = baseName + "_" + uniqueID + fileExtension;
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String filePart = UPLOAD_PATH + File.separator;
            log.info(filePart);
            try {
                fileAvatar.transferTo(new File(filePart, fileName));
                String avatarUrl = PATH_AVATAR + fileName;
                account.setAvatar(avatarUrl);
                accountsRepository.save(account);
                return avatarUrl;
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new AppException(ErrorCode.INVALID_FILE_NULL);
            }
        }
        accountsRepository.save(account);
        return account.getAvatar();
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

    public void setPasswordPrompt(SetPasswordPromptRequest request) {
        var account = securityService.getAccountByJWT();
        account.setSkip_password_prompt(request.isSkip());
        log.info("Is: {}", request.isSkip());
        accountsRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void upRole(MemberRoleUpRequest request) {
        int accID = request.getAccID();
        String newRole = request.getRole();
        Account account = accountsRepository.findById(accID).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        account.setRole(Role.valueOf(newRole));
        accountsRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> banAccount(int accountId) {
        var currentAdmin = securityService.getAccountByJWT();
        if (currentAdmin.getAccID() == accountId) {
            throw new AppException(ErrorCode.CANNOT_BAN_OWN_ACCOUNT);
        }
        Account account =
                accountsRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT));
        if (account.isBanned()) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_BANNED);
        }
        account.setBanned(true);
        accountsRepository.save(account);
        return ApiResponse.<Void>builder().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> unlockAccount(int accountId) {
        Account account =
                accountsRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT));
        if (!account.isBanned()) {
            return ApiResponse.<Void>builder().build();
        }
        account.setBanned(false);
        accountsRepository.save(account);
        return ApiResponse.<Void>builder().build();
    }

    public ForgotPasswordResponse getEmailPhoneByUserName(ForgotPasswordRequest request) {
        return accountMapper.toForgotPasswordResponse(accountsRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ACCOUNT)));
    }

    public void reChargeAmount (String sms) {
        Pattern amountPattern = Pattern.compile("GD: [+-](\\d{1,3}(,\\d{3})*)VND");
        Matcher amountMatcher = amountPattern.matcher(sms);
        Pattern userIdPattern = Pattern.compile("NAPBOOK (\\d+)");
        Matcher userIdMatcher = userIdPattern.matcher(sms);

        if (amountMatcher.find() && userIdMatcher.find()) {
            String amountStr = amountMatcher.group(1).replace(",", "");
            int amount = Integer.parseInt(amountStr);
            int accID = Integer.parseInt(userIdMatcher.group(1));
            Account account = accountsRepository.findById(accID).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            account.setAmount(account.getAmount() + amount );
            accountsRepository.save(account);
        } else {
            log.info("Invalid amount");
        }
    }
    public ListAccountForAdminResponse getAllAccountForAdmin(int num, int page ,int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "accID"));
        if(num ==7 || num ==8) {
            var list = accountsRepository.findAllByIsBanned(num ==7 ? true :false ,pageable);
            return ListAccountForAdminResponse.builder()
                    .accounts(list.getContent())
                    .totalPages(list.getTotalPages())
                    .currentPage(list.getNumber())
                    .countOrderByRole(getRoleCounts())
                    .listCountByActiveStatus(getActiveStatusCounts())
                    .countOrderByPayment(getMemberTypeCounts())
                    .build();
        }
        if(num == -1 ) {
            var list = accountsRepository.findAll(pageable);
            return ListAccountForAdminResponse.builder()
                    .accounts(list.getContent())
                    .totalPages(list.getTotalPages())
                    .currentPage(list.getNumber())
                    .countOrderByRole(getRoleCounts())
                    .listCountByActiveStatus(getActiveStatusCounts())
                    .countOrderByPayment(getMemberTypeCounts())
                    .build();
        }
        if (num <=3 ) {
            var list = accountsRepository.findAllByMemberType(MemberType.values()[num], pageable);
            return ListAccountForAdminResponse.builder()
                    .accounts(list.getContent())
                    .totalPages(list.getTotalPages())
                    .currentPage(list.getNumber())
                    .countOrderByRole(getRoleCounts())
                    .listCountByActiveStatus(getActiveStatusCounts())
                    .countOrderByPayment(getMemberTypeCounts())
                    .build();
        }
        var list = accountsRepository.findAllByRole( num == 4 ? Role.USER : Role.ADMIN , pageable);
        return ListAccountForAdminResponse.builder()
                .accounts(list.getContent())
                .totalPages(list.getTotalPages())
                .currentPage(list.getNumber())
                .countOrderByRole(getRoleCounts())
                .listCountByActiveStatus(getActiveStatusCounts())
                .countOrderByPayment(getMemberTypeCounts())
                .build();
    }
    private List<CountAccountByMemberType> getMemberTypeCounts() {
        List<Object[]> rawCounts = accountsRepository.countMemberType();

        Map<MemberType, Long> countsMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> (MemberType) row[0],
                        row -> (Long) row[1]
                ));
        for (MemberType type : MemberType.values()) {
            countsMap.putIfAbsent(type, 0L);
        }

        return countsMap.entrySet().stream()
                .map(entry -> CountAccountByMemberType.builder()
                        .memberType(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
    private List<CountAccountByRole> getRoleCounts() {
        List<Object[]> rawCounts = accountsRepository.countRole();

        Map<Role, Long> countsMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> (Role) row[0],
                        row -> (Long) row[1]
                ));
        for (Role type : Role.values()) {
            countsMap.putIfAbsent(type, 0L);
        }

        return countsMap.entrySet().stream()
                .map(entry -> CountAccountByRole.builder()
                        .role(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
    private List<CountByActiveStatus> getActiveStatusCounts() {
        List<Object[]> rawCounts = accountsRepository.countByIsBannedStatus();

        Map<Boolean, Long> countsMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> (Boolean) row[0],
                        row -> (Long) row[1]
                ));
        countsMap.putIfAbsent(true, 0L);
        countsMap.putIfAbsent(false, 0L);

        return countsMap.entrySet().stream()
                .map(entry -> CountByActiveStatus.builder()
                        .isBanned(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public searchAccountResponse searchAccounts(String param) {
        return searchAccountResponse.builder()
                .accounts(accountsRepository.searchAccounts(param))
                .build();
    }
    public void changeRole(int id) {
        var account = accountsRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if(account.getRole() == Role.ADMIN) {
            account.setRole(Role.USER);
            accountsRepository.save(account);
            return;
        }
        account.setRole(Role.ADMIN);
        accountsRepository.save(account);
    }
    public void changStatus(int id) {
        var account = accountsRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if(account.isBanned()) {
            account.setBanned(false);
            accountsRepository.save(account);
            return;
        }
        account.setBanned(true);
        accountsRepository.save(account);;
    }
    public Account getAccountByID(int id ) {
        return accountsRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
}
