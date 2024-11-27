package thebook.fshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import thebook.fshop.DTO.Request.CheckQROrderRequest;
import thebook.fshop.DTO.Request.GetQrMemberShipRequest;
import thebook.fshop.DTO.Request.OrderCreationRequest;
import thebook.fshop.DTO.Response.QRCodeResponse;
import thebook.fshop.DTO.Response.StatusQROrderResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Transaction;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.*;
import thebook.fshop.repository.AccountsRepository;
import thebook.fshop.repository.MembershipPackageRepository;
import thebook.fshop.repository.TransactionRepository;
import thebook.fshop.repository.UserMemberShipRepository;

import java.lang.reflect.Member;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentQRService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    CartService cartService;
    RedisTemplate<String, Object> template;
    RedisTemplate<String, PaymentInfo> templatePaymentInfo;
    RedisTemplate<String, MemberShipTemp> templateMemberShip;
    MembershipPackageService membershipPackageService;
    SecurityService securityService;
    MembershipPackageRepository membershipPackageRepository;
    AccountService accountService;
    AccountsRepository accountsRepository;
    OrderService orderService;
    UserMemberShipRepository userMemberShipRepository;
    TransactionRepository transactionRepository;

    public static String generateQRUrl(String description, double amount) {
        String bankId = "mbbank";
        String accountNo = "0848150667";
        String accountName = "Nguyễn Khánh Duy";
        String template = "print";
        String qrUrl = String.format(
                "https://img.vietqr.io/image/%s-%s-%s.png?amount=%.2f&addInfo=%s&accountName=%s",
                bankId,
                accountNo,
                template,
                amount,
                description,
                accountName
        );

        return qrUrl;
    }

    public static String generateUniquePaymentId(String string) {
        StringBuilder uniqueId = new StringBuilder(string);

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(CHARACTERS.length());
            uniqueId.append(CHARACTERS.charAt(index));
        }

        return uniqueId.toString();
    }

    public QRCodeResponse createQrURLAndSave(OrderCreationRequest temp) {
       var cart =  cartService.viewCart();
       var price = cart.getTotalPriceAfterSale();
       var carID = cart.getCartID();
        log.info("cart : {}", cart);
      var message = generateUniquePaymentId("ThanhToanDH "+carID).trim();
      var paymentInfo = PaymentInfo.builder()
              .price(price)
              .accID(cart.getAccount().getAccID())
              .tempAddress(temp)
              .status(false)
              .build();
        templatePaymentInfo.opsForValue().set(message, paymentInfo);
        templatePaymentInfo.expire(message, 900, TimeUnit.SECONDS);
        return QRCodeResponse.builder()
                .qrUrl(generateQRUrl(message, price))
                .messageKey(message)
                .build();

    }
    public void processSMS(String sms) {
        log.info(sms);
        Pattern amountPattern = Pattern.compile("GD: [+-]?(\\d{1,3}(?:,\\d{3})*)VND");
        Matcher amountMatcher = amountPattern.matcher(sms);

        Pattern transactionPattern = Pattern.compile("(NapBook|MuaGoi|ThanhToanDH) (\\S+)");
        Matcher transactionMatcher = transactionPattern.matcher(sms);

        if (amountMatcher.find() && transactionMatcher.find()) {
            String amountStr = amountMatcher.group(1).replace(",", "");
            int amount = Integer.parseInt(amountStr);
            String transactionType = transactionMatcher.group(1);
            String ID = transactionMatcher.group(2);
            log.info("Amount : {}",amount);
            log.info("Transaction Type : {}",transactionType);
            log.info("ID: {}", ID);
            switch (transactionType) {
                case "NapBook":
                    handleAddFunds(ID, amount);
                    break;
                case "MuaGoi":
                    handlePackagePurchase(ID,amount);
                    break;
                case "ThanhToanDH":
                    handleOrderPayment(ID, amount);
                    break;
                default:
                    return;
            }

        } else {
            log.info("Invalid SMS format or amount");
        }
    }

    private void handlePackagePurchase(String message, long amount) {
        var key = "MuaGoi " + message;
        MemberShipTemp memberShipTemp  = templateMemberShip.opsForValue().get(key);
        if (memberShipTemp == null) throw new AppException(ErrorCode.NOT_FOUND);
        if(amount != memberShipTemp.getPrice()) throw new AppException(ErrorCode.INVALID_PRICE_QR);
        memberShipTemp.setStatus(true);
        templateMemberShip.opsForValue().set(key, memberShipTemp);
        membershipPackageService.handlePaymentQRPackage(memberShipTemp.getAccount(), memberShipTemp.getMembershipPackage(), amount);
    }

    private void handleOrderPayment(String message, int amount) {
        var key = "ThanhToanDH " + message;
        PaymentInfo paymentInfo = templatePaymentInfo.opsForValue().get(key);

        if(paymentInfo == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        if(amount != paymentInfo.getPrice()) {
            throw new AppException(ErrorCode.INVALID_PRICE_QR);
        }
        paymentInfo.setStatus(true);
        templatePaymentInfo.opsForValue().set(key, paymentInfo);
        orderService.createOrderFromCart(paymentInfo.getTempAddress());
    }

    private void handleAddFunds(String accIDS, long amount) {
        var accID= -1;
       try {
            accID = Integer.parseInt(accIDS);
       } catch (NumberFormatException e) {
           throw new AppException(ErrorCode.NOT_FOUND);
        }
        var account = accountsRepository.findById(accID).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
       var beforeAccount = account.getAmount();
        account.setAmount(account.getAmount() + amount);
        var trans = createTransaction(account, beforeAccount,MethodType.QR_CODE,TransactionType.DEPOSIT,amount);
        transactionRepository.save(trans);
        accountsRepository.save(account);
    }
    public Transaction createTransaction(Account account, long beforeAmount, MethodType methodType, TransactionType transactionType, long price ) {
        String content = "Nạp tiền vào website" ;
        return Transaction.builder()
                .account(account)
                .beforeAmount(beforeAmount)
                .afterAmount(account.getAmount())
                .methodType(methodType)
                .content(content)
                .priceQR(price)
                .time(new Date())
                .transactionType(transactionType)
                .build();
    }

    public StatusQROrderResponse checkStatusQRBank(CheckQROrderRequest request) {
        PaymentInfo paymentInfo = templatePaymentInfo.opsForValue().get(request.getMessageKey());
        if(paymentInfo == null) throw new AppException(ErrorCode.NOT_FOUND);
        log.info("paymentInfo: {}", paymentInfo);
         return StatusQROrderResponse.builder()
                 .status(paymentInfo.isStatus())
                 .build();
    }
    public QRCodeResponse getQrURL(GetQrMemberShipRequest request) {
      var account = securityService.getAccountByJWT();
      var memberPackage = membershipPackageRepository.findById(request.getMemberShipPackageID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
      var packageOfUser  = userMemberShipRepository.findByAccount_AccID(account.getAccID());
      long totalPrice =  memberPackage.getPrice();
      if(packageOfUser != null) {
          totalPrice =memberPackage.getPrice() - membershipPackageService.getTotalSale(packageOfUser,memberPackage);
      }
        var message = generateUniquePaymentId("MuaGoi "+account.getAccID()).trim();
        var memberShipTemp = MemberShipTemp.builder()
                .price(totalPrice)
                .account(account)
                .status(false)
                .membershipPackage(memberPackage)
                .build();
        templateMemberShip.opsForValue().set(message, memberShipTemp);
        templateMemberShip.expire(message, 900, TimeUnit.SECONDS);
        return QRCodeResponse.builder()
                .qrUrl(generateQRUrl(message, totalPrice))
                .messageKey(message)
                .build();
    }

    public StatusQROrderResponse checkPaymentStatusQRBank(CheckQROrderRequest request) {
        MemberShipTemp memberShipTemp  = templateMemberShip.opsForValue().get(request.getMessageKey());
        if(memberShipTemp == null) throw new AppException(ErrorCode.NOT_FOUND);
        log.info("memberShipTemp: {}", memberShipTemp);
        return StatusQROrderResponse.builder()
                .status(memberShipTemp.isStatus())
                .build();
    }

}
