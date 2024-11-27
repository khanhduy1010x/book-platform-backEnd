package thebook.fshop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddVoucherRequest;
import thebook.fshop.DTO.Response.ListVoucherResponse;
import thebook.fshop.DTO.Response.VoucherForAdminResponse;
import thebook.fshop.DTO.Response.VoucherResponse;
import thebook.fshop.entity.CartItem;
import thebook.fshop.entity.Voucher;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.CartItemRepository;
import thebook.fshop.repository.CartRepository;
import thebook.fshop.repository.UserVoucherRepository;
import thebook.fshop.repository.VoucherRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherService {
    VoucherRepository voucherRepository;
    SecurityService securityService;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    UserVoucherRepository userVoucherRepository;

    public List<VoucherResponse> getVoucherForCart() {
        var account = securityService.getAccountByJWT();
        var cart = cartRepository
                .findByAccount_AccID(account.getAccID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        var totalInCart = cartItemRepository.findByCart_IDOrderByIDAsc(cart.getID()).stream()
                .mapToLong(
                        cartItem -> cartItem.getQuantity() * cartItem.getBook().getPrice())
                .sum();
        var listAllVoucher = voucherRepository.findAll();
        var voucherPrivate = userVoucherRepository.findByAccount_AccID(account.getAccID());
        if (listAllVoucher.isEmpty()) {
            voucherPrivate.stream().forEach((item) ->
                            listAllVoucher.add(item.getVoucher())
                    );
        }
        var listVoucherAvailable = listAllVoucher.stream()
                .filter(voucher ->  voucher.getMinCartValue() <= totalInCart
                        && voucher.getMemberType().ordinal() <= account.getMemberType().ordinal()
                )
                .toList();
        long publicVoucherCount = cart.getVouchers().stream().filter(Voucher::isPublic).count();
        boolean hasNonPublicVoucherInCart = cart.getVouchers().stream().anyMatch(v -> !v.isPublic());
        return listAllVoucher.stream()
                .map(voucher -> {
                    boolean isAvailable;
                    if (voucher.isPublic()) {
                        isAvailable = listVoucherAvailable.contains(voucher) && publicVoucherCount < 3;
                    } else {
                        isAvailable = !hasNonPublicVoucherInCart && listVoucherAvailable.contains(voucher) ;
                    }
                    return new VoucherResponse(voucher, isAvailable, cart.getVouchers().contains(voucher));
                })
                .collect(Collectors.toList());
    }

    public void addVoucher(AddVoucherRequest addVoucherRequest) {
        var account = securityService.getAccountByJWT();
        var voucher = voucherRepository.findById(addVoucherRequest.getVoucherID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        var cart = cartRepository.findByAccount_AccID(account.getAccID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if (cart.getVouchers().contains(voucher)) {
            cart.getVouchers().remove(voucher);
            cartRepository.save(cart);
            return;
        }
        long publicVoucherCount = cart.getVouchers().stream().filter(Voucher::isPublic).count();
        boolean hasNonPublicVoucherInCart = cart.getVouchers().stream().anyMatch(v -> !v.isPublic());
        if ((voucher.isPublic() && publicVoucherCount >= 3) || (!voucher.isPublic() && hasNonPublicVoucherInCart)) {
            throw new AppException(ErrorCode.MAX_VOUCHER_LIMIT_REACHED);
        }
        var listCartItem = cartItemRepository.findByCart_ID(cart.getID());
        var totalInCart = listCartItem.stream().mapToLong((cartItem) -> cartItem.getQuantity() * cartItem.getBook().getPrice()).sum();
        if (voucher.getMemberType().ordinal() > account.getMemberType().ordinal())
            throw new AppException(ErrorCode.VOUCHER_MEMBER_TYPE_INVALID);
        if (totalInCart < voucher.getMinCartValue()) throw new AppException(ErrorCode.NOT_ENOUGH_TOTAL_PRICE);
        cart.getVouchers().add(voucher);
        cartRepository.save(cart);
    }
    public VoucherForAdminResponse getAllVouchers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ID"));
        var list =  voucherRepository.findAll(pageable);
        return VoucherForAdminResponse.builder()
                .listVoucher(list.getContent())
                .currentPage(list.getNumber())
                .totalPages(list.getTotalPages())
                .build();
    }
    public ListVoucherResponse searchVoucher(String param) {
        return ListVoucherResponse.builder()
                .listVoucher(voucherRepository.findByVoucherNameWithAndWithoutUnaccent(param))
                .build();
    }
    public void addNewVoucher(Voucher voucher) {
        voucherRepository.save(voucher);
    }
    public void editVoucher(Voucher voucher) {
        voucherRepository.save(voucher);
    }
}
