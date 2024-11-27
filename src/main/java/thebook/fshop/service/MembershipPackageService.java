package thebook.fshop.service;

import jakarta.servlet.ServletConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.BuyPackageRequest;
import thebook.fshop.DTO.Response.CountMemberByType;
import thebook.fshop.DTO.Response.CountOrderByPayment;
import thebook.fshop.DTO.Response.MemberPackageForAdminResponse;
import thebook.fshop.DTO.Response.SaleForPackageResponse;
import thebook.fshop.entity.*;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.MemberType;
import thebook.fshop.helper.MethodType;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.TransactionType;
import thebook.fshop.repository.AccountsRepository;
import thebook.fshop.repository.MembershipPackageRepository;
import thebook.fshop.repository.TransactionRepository;
import thebook.fshop.repository.UserMemberShipRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static thebook.fshop.helper.MemberType.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MembershipPackageService {

    MembershipPackageRepository membershipPackageRepository;
    SecurityService securityService;
    UserMemberShipRepository userMemberShipRepository;
    AccountsRepository accountsRepository;
    NotificationService notificationService;
    TransactionRepository transactionRepository;
    private final ServletConfig servletConfig;

    public List<MembershipPackage> getAllPackage() {
        return membershipPackageRepository.findAll();
    }

    public void buyMemberShipPackage(BuyPackageRequest request) {
        var account = securityService.getAccountByJWT();
        var beforeAmount = account.getAmount();
        var memberPackage = membershipPackageRepository.findById(request.getPackageID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if(account.getMemberType().ordinal() > memberPackage.getMemberType().ordinal()) throw new AppException(ErrorCode.LOWER_PACKAGE_PURCHASE_NOT_ALLOWED);
        if(account.getAmount() < memberPackage.getPrice()) throw new AppException(ErrorCode.NOT_ENOUGH_TOTAL_PRICE);
        var packageOfUser = userMemberShipRepository.findByAccount_AccID(account.getAccID());
        var totalPrice = memberPackage.getPrice();
        if( packageOfUser !=null) {
            var salePrice = getTotalSale(packageOfUser,memberPackage);
             totalPrice =memberPackage.getPrice()- salePrice;
        }
        account.setAmount(account.getAmount() - totalPrice);
        LocalDate endDateLocal = LocalDate.now().plusDays(memberPackage.getDurationDays());
        Date endDate = Date.from(endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date startDate = new Date();
        if(account.getMemberType() == memberPackage.getMemberType()) {
            startDate = packageOfUser.getStartDate();
            int additionalDays = memberPackage.getDurationDays();
            LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newEndDate = localEndDate.plusDays(additionalDays);
            endDate = Date.from(newEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        if(packageOfUser != null) {
            packageOfUser.setEndDate(endDate);
            packageOfUser.setStartDate(startDate);
            packageOfUser.setMembershipPackage(memberPackage);
            userMemberShipRepository.save(packageOfUser);
        }else {
            var userMemberShip = UserMemberShip.builder()
                    .account(account)
                    .membershipPackage(memberPackage)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            userMemberShipRepository.save(userMemberShip);
        }
       var trans = createTransaction(account,memberPackage.getMemberType(),beforeAmount,memberPackage.getDurationDays(),MethodType.WEBSITE,TransactionType.MEMBER_PACKAGE,totalPrice);
        transactionRepository.save(trans);
        membershipPackageRepository.save(memberPackage);
        account.setMemberType(memberPackage.getMemberType());
        accountsRepository.save(account);
        var notification = Notification.builder()
                .account(account)
                .createAt(new Date())
                .isRead(false)
                .title("Mua gói thành viên thành công !")
                .message("Chúc mừng bạn đã mua gói thành công! Tài khoản của bạn hiện đã được kích hoạt với những quyền lợi và ưu đãi đặc biệt. Hãy tận hưởng trải nghiệm dịch vụ tốt nhất mà chúng tôi mang đến cho bạn. Nếu cần hỗ trợ hoặc có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi. Cảm ơn bạn đã tin tưởng và đồng hành cùng chúng tôi!")
                .build();
        notificationService.saveNotification(notification);
    }

    public SaleForPackageResponse getSaleForPackage(int id) {
        var account = securityService.getAccountByJWT();
        var memberPackage = membershipPackageRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if (memberPackage.getMemberType().ordinal() < account.getMemberType().ordinal()) {
            return SaleForPackageResponse.builder()
                    .salePrice(0)
                    .build();
        } else if (memberPackage.getMemberType().ordinal() > account.getMemberType().ordinal()) {
            var memberShipOfUser = userMemberShipRepository.findByAccount_AccID(account.getAccID());
            if (memberShipOfUser == null) {
                return SaleForPackageResponse.builder()
                        .salePrice(0)
                        .build();
            }
            long totalSale = getTotalSale(memberShipOfUser, memberPackage);
            return SaleForPackageResponse.builder()
                    .salePrice(totalSale)
                    .build();
        }
        return SaleForPackageResponse.builder()
                .salePrice(0)
                .build();

    }




    public long getTotalSale(UserMemberShip memberShipOfUser, MembershipPackage memberPackage) {
        var priceOneDay = memberShipOfUser.getMembershipPackage().getPrice() / memberShipOfUser.getMembershipPackage().getDurationDays();
        Date expiredPackageDay = memberShipOfUser.getEndDate();
        Date now = new Date();
        long diff = Math.abs(now.getTime() - expiredPackageDay.getTime());
        int datesBetween = (int) (diff / (24 * 60 * 60 * 1000));
        long totalSale = priceOneDay * datesBetween;
        long maxSale = (long) (memberPackage.getPrice() * 0.2);
        return Math.min(totalSale, maxSale);
    }
    public void handlePaymentQRPackage(Account account, MembershipPackage membershipPackage, long amount){
        var packageOfUser = userMemberShipRepository.findByAccount_AccID(account.getAccID());
        LocalDate endDateLocal = LocalDate.now().plusDays(membershipPackage.getDurationDays());
        Date endDate = Date.from(endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date startDate = new Date();
        if(account.getMemberType() == membershipPackage.getMemberType()) {
            startDate = packageOfUser.getStartDate();
            int additionalDays = membershipPackage.getDurationDays();
            LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newEndDate = localEndDate.plusDays(additionalDays);
            endDate = Date.from(newEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        if(packageOfUser != null) {
            packageOfUser.setEndDate(endDate);
            packageOfUser.setStartDate(startDate);
            packageOfUser.setMembershipPackage(membershipPackage);
            userMemberShipRepository.save(packageOfUser);
        }else {
            var userMemberShip = UserMemberShip.builder()
                    .account(account)
                    .membershipPackage(membershipPackage)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            userMemberShipRepository.save(userMemberShip);
        }
        account.setMemberType(membershipPackage.getMemberType());
        accountsRepository.save(account);
        var notification = Notification.builder()
                .account(account)
                .createAt(new Date())
                .isRead(false)
                .title("Mua gói thành viên thành công !")
                .message("Chúc mừng bạn đã mua gói thành công! Tài khoản của bạn hiện đã được kích hoạt với những quyền lợi và ưu đãi đặc biệt. Hãy tận hưởng trải nghiệm dịch vụ tốt nhất mà chúng tôi mang đến cho bạn. Nếu cần hỗ trợ hoặc có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi. Cảm ơn bạn đã tin tưởng và đồng hành cùng chúng tôi!")
                .build();

        notificationService.saveNotification(notification);
        var trans = createTransaction(account,membershipPackage.getMemberType(),account.getAmount(),membershipPackage.getDurationDays(),MethodType.QR_CODE,TransactionType.MEMBER_PACKAGE,amount);
        transactionRepository.save(trans);

    }
    public Transaction createTransaction(Account account, MemberType memberType, long beforeAmount, int date, MethodType methodType, TransactionType transactionType, long price ) {
        String packageName;
        switch (memberType) {
            case BASIC:
                packageName = "Bạc";
                break;
            case ADVANCE:
                packageName = "Vàng";
                break;
            case PREMIUM:
                packageName = "Kim cương";
                break;
            default:
                packageName = "không xác định";
        }

        String content = "Mua gói thành viên " + packageName + " " +date + " ngày";

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

    public MemberPackageForAdminResponse getAllMPForAdmin (int num, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "ID"));
        if(num == -1 ) {
            var list = membershipPackageRepository.findAll(pageable);
            return  MemberPackageForAdminResponse.builder()
                    .membershipPackages(list.getContent())
                    .currentPage(list.getNumber())
                    .totalPages(list.getTotalPages())
                    .listMemberByType(getPaymentStatusCounts())
                    .build();
        }
        var list = membershipPackageRepository.findAllByMemberType(MemberType.values()[num],pageable);
        return  MemberPackageForAdminResponse.builder()
                .membershipPackages(list.getContent())
                .currentPage(list.getNumber())
                .totalPages(list.getTotalPages())
                .listMemberByType(getPaymentStatusCounts())
                .build();

    }
    private List<CountMemberByType> getPaymentStatusCounts() {
        List<Object[]> rawCounts = membershipPackageRepository.countAllMemberType();

        Map<MemberType, Long> countsMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> (MemberType) row[0],
                        row -> (Long) row[1]
                ));
        for (MemberType type : MemberType.values()) {
            countsMap.putIfAbsent(type, 0L);
        }

        return countsMap.entrySet().stream()
                .map(entry -> CountMemberByType.builder()
                        .memberType(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
    public void addNewMemberPackage(MembershipPackage membershipPackage) {
        membershipPackageRepository.save(membershipPackage);
    }
    public void editPkg(MembershipPackage membershipPackage) {
        membershipPackageRepository.save(membershipPackage);
    }
}
