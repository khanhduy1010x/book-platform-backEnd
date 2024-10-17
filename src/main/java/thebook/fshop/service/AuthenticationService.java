package thebook.fshop.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import thebook.fshop.DTO.Request.*;
import thebook.fshop.DTO.Response.AuthenticationResponse;
import thebook.fshop.DTO.Response.IntrorespectResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.InvalidToken;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.LoginType;
import thebook.fshop.helper.MemberType;
import thebook.fshop.helper.Role;
import thebook.fshop.repository.AccountsRepository;
import thebook.fshop.repository.InvalidateTokenRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    private WebClient webClient = WebClient.create();
    private RedisTemplate<String, Object> template;
    SecurityService securityService;
    EmailService emailService;
    AccountsRepository accountsRepository;
    InvalidateTokenRepository invalidateRepository;

    @NonFinal
    @Value("${upload.path}")
    String UPLOAD_PATH;

    @NonFinal
    @Value("${path.avatar}")
    String PATH_AVATAR;

    @NonFinal
    @Value("${sms.key}")
    protected String SMS_KEY;

    @NonFinal
    @Value("${sms.device}")
    protected String DEVICE_KEY;

    private String uriSendSMS = "https://api.speedsms.vn/index.php/sms/send";

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_REFRESH;

    public IntrorespectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            var jwt = verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrorespectResponse.builder().valid(isValid).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<Account> accounts = accountsRepository
                .findByPhone(request.getPhoneOrMail());
        if(accounts.isEmpty()){
            accounts = accountsRepository.findByEmail(request.getPhoneOrMail());
            if(accounts.isEmpty())throw new AppException(ErrorCode.INVALID_USERNAME);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), accounts.get().getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        var tokenData = generate(accounts.get());
        return AuthenticationResponse.builder()
                .token(tokenData.getToken())
                .expiryTime(tokenData.getExpiryTime())
                .refreshedTime(tokenData.getRefreshedTime())
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();

            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidToken invalidToken =
                    InvalidToken.builder().id(jit).expiryDate(expiryTime).build();
            invalidateRepository.save(invalidToken);
        } catch (AppException e) {
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(), true);
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken =
                InvalidToken.builder().id(jit).expiryDate(expiryTime).build();
        invalidateRepository.save(invalidToken);
        var phone = signToken.getJWTClaimsSet().getSubject();
        var account = accountsRepository.findByPhone(phone).orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        var tokenData = generate(account);

        return AuthenticationResponse.builder()
                .token(tokenData.getToken())
                .expiryTime(tokenData.getExpiryTime())
                .refreshedTime(tokenData.getRefreshedTime())
                .authenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expireTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_REFRESH, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if ((!verified || !expireTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);
        if (invalidateRepository.existsById(
                signedJWT.getJWTClaimsSet().getJWTID().toString())) throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    private AuthenticationResponse generate(Account account) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        Date expiryTime =
                new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli());
        Date reFreshTime = new Date(
                Instant.now().plus(REFRESHABLE_REFRESH, ChronoUnit.SECONDS).toEpochMilli());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getPhone())
                .issuer("KhanhDuy")
                .jwtID(UUID.randomUUID().toString())
                .issueTime(new Date())
                .expirationTime(expiryTime)
                .claim("scope", buildScope(account))
                .claim("email", account.getEmail())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return AuthenticationResponse.builder()
                    .token(jwsObject.serialize())
                    .expiryTime(expiryTime)
                    .refreshedTime(reFreshTime)
                    .build();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.ERROR_GENERATE_TOKEN);
        }
    }

    private String buildScope(Account account) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (account.getRole() != null) stringJoiner.add(account.getRole().toString());
        return stringJoiner.toString();
    }

    public void sendOTPSMS(SendOTPRequest request) {
        String phone = request.getPhone();
        if (accountsRepository.existsByPhone(phone)) throw new AppException(ErrorCode.EXITS_PHONE);
        if (template.getExpire(phone, TimeUnit.SECONDS) > 0) throw new AppException(ErrorCode.WAITING_TIME);
        Random rand = new Random();
        int otp = rand.nextInt(900000) + 100000;
        template.opsForValue().set(phone, String.valueOf(otp));
        template.expire(phone, 1200, TimeUnit.SECONDS);
        //        SendSMSServer sendSMSServer = SendSMSServer.builder()
        //                .content("Book4.0 - Mã OTP kích hoạt số điện thoại của bạn là: " + otp)
        //                .to(phone)
        //                .sender(DEVICE_KEY)
        //                .build();
        //        webClient
        //                .post()
        //                .uri(uriSendSMS)
        //                .contentType(MediaType.APPLICATION_JSON)
        //                .bodyValue(sendSMSServer)
        //                .headers(headers -> headers.setBasicAuth(SMS_KEY, ""))
        //                .retrieve()
        //                .bodyToMono(Map.class)
        //                .subscribe(
        //                        response -> {
        //                            log.info(response.toString());
        //                            String status = (String) response.get("status");
        //                            if (!"success".equals(status)) {
        //                                throw new AppException(ErrorCode.ERROR_SEND);
        //                            }
        //                            log.info(template.getExpire(phone, TimeUnit.SECONDS).toString());
        //                            template.opsForValue().set(phone, String.valueOf(otp));
        //                            template.expire(phone, 1200, TimeUnit.SECONDS);
        //                        },
        //                        error -> {
        //                            throw new AppException(ErrorCode.ERROR_SEND);
        //                        });
    }

    public void changePassword(ChangePasswordRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var account = securityService.getAccountByJWT();
        if (!request.getNewPassword().equals(request.getConfirmationNewPassword())) {
            throw new AppException(ErrorCode.INVALID_NEW_PASSWORD);
        }
        if (passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            account.setPassword(passwordEncoder.encode(request.getConfirmationNewPassword()));
            accountsRepository.save(account);
        } else {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

    public Mono<AuthenticationResponse> getUserByGoogleToken(GoogleLoginRequest request) {
        log.info("in service");
        return webClient
                .get()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .headers(headers -> headers.setBearerAuth(request.getToken()))
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    String email = (String) response.get("email");
                    Optional<Account> accountExits = accountsRepository.findByEmail(email);
                    if (accountExits.isEmpty()) {
                        String name = (String) response.get("name");
                        String avatarImageURL = (String) response.get("picture");
                        String uniqueId = UUID.randomUUID().toString();
                        String fileName = "avatar_" + uniqueId + ".jpg";
                        String savedAvatarPath = "";
                        try {
                            URL url = new URL(avatarImageURL);
                            InputStream inputStream = url.openStream();
                            File uploadDir = new File(UPLOAD_PATH);
                            if (!uploadDir.exists()) {
                                uploadDir.mkdirs();
                            }
                            File file = new File(UPLOAD_PATH + File.separator + fileName);
                            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            inputStream.close();
                            savedAvatarPath = PATH_AVATAR + fileName;
                        } catch (IOException e) {
                            return Mono.error(new AppException(ErrorCode.SERVER_ERROR));
                        }
                        var account = Account.builder()
                                .email(email)
                                .avatar(savedAvatarPath)
                                .role(Role.USER)
                                .amount(0L)
                                .memberType(MemberType.NONE)
                                .fullName(name)
                                .loginType(LoginType.GOOGLE)
                                .build();
                        accountsRepository.save(account);
                        var tokenData = generate(account);
                        return Mono.just(AuthenticationResponse.builder()
                                .token(tokenData.getToken())
                                .expiryTime(tokenData.getExpiryTime())
                                .refreshedTime(tokenData.getRefreshedTime())
                                .authenticated(true)
                                .build());
                    } else {
                        var tokenData = generate(accountExits.get());
                        return Mono.just(AuthenticationResponse.builder()
                                .token(tokenData.getToken())
                                .expiryTime(tokenData.getExpiryTime())
                                .refreshedTime(tokenData.getRefreshedTime())
                                .authenticated(true)
                                .build());
                    }
                })
                .onErrorResume(error -> {
                    log.info(error.getMessage());
                    return Mono.error(new AppException(ErrorCode.SERVER_ERROR));
                });
    }
    public void createPassword(CreatePasswordRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var account = securityService.getAccountByJWT();
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        accountsRepository.save(account);
    }
    public void forgotPassword (ForgotPasswordRequest request) throws MessagingException {
        Account account = accountsRepository.findByPhone(request.getUsername())
                .orElseGet(() -> accountsRepository.findByEmail(request.getUsername()).orElse(null));
        boolean isPhone = account != null && accountsRepository.findByPhone(request.getUsername()).isPresent();
        if (account == null) throw new AppException(ErrorCode.NOT_EXITS_ACCOUNT);
        Random rand = new Random();
        int otp = rand.nextInt(900000) + 100000;
        if(!isPhone){
            emailService.sendEmail(account.getFullName(), request.getUsername(), "OTP Đặt lại mật khẩu Book4.0",String.valueOf(otp));
            template.opsForValue().set(String.valueOf(account.getAccID()), String.valueOf(otp));
            template.expire(String.valueOf(account.getAccID()), 1200, TimeUnit.SECONDS);
        }else {
            SendSMSServer sendSMSServer = SendSMSServer.builder()
                            .content("Book4.0 - Mã OTP đặt lại mật khẩu của bạn là: " + otp)
                            .to(request.getUsername())
                            .sender(DEVICE_KEY)
                            .build();
                    webClient
                            .post()
                            .uri(uriSendSMS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(sendSMSServer)
                            .headers(headers -> headers.setBasicAuth(SMS_KEY, ""))
                            .retrieve()
                            .bodyToMono(Map.class)
                            .subscribe(
                                    response -> {
                                        String status = (String) response.get("status");
                                        if (!"success".equals(status)) {
                                            throw new AppException(ErrorCode.ERROR_SEND);
                                        }
                                        log.info(template.getExpire(request.getUsername(), TimeUnit.SECONDS).toString());
                                        template.opsForValue().set(request.getUsername(), String.valueOf(otp));
                                        template.expire(request.getUsername(), 1200, TimeUnit.SECONDS);
                                    },
                                    error -> {
                                        throw new AppException(ErrorCode.ERROR_SEND);
                                    });
        }
    }
}
