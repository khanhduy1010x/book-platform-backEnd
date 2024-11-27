package thebook.fshop.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.ChangeStatusOrderRequest;
import thebook.fshop.DTO.Request.OrderCreationRequest;
import thebook.fshop.DTO.Response.*;
import thebook.fshop.entity.*;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.DiscountType;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;
import thebook.fshop.mapper.OrderMapper;
import thebook.fshop.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService {
    SecurityService securityService;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    ShipInfoRepository shipInfoRepository;
    AccountsRepository accountsRepository;
    OrderMapper orderMapper;
    NotificationService notificationService;
    @Transactional
    public void createOrderFromCart(OrderCreationRequest request) {
        Account account;
        log.info("in here");
        log.info("Request: {}",request);
        if(request.getAccID()!= null) {
            log.info("in !null");
            account = accountsRepository.findById(request.getAccID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        } else {
            account = securityService.getAccountByJWT();
        }
        Cart cart = cartRepository.findByAccount_AccID(account.getAccID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findByCart_ID(cart.getID());

        if (cartItems.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);        }

        long totalAmount = cartItems.stream()
                .mapToLong(cartItem -> cartItem.getBook().getPrice() * cartItem.getQuantity())
                .sum();
        long totalSale = 0;
        var listVoucher = cart.getVouchers();
        if (!listVoucher.isEmpty()) {
            for (Voucher v : listVoucher) {
                if (v.getDiscountType() == DiscountType.PERCENTAGE) {
                    totalSale += (v.getDiscountValue()/100) * totalAmount; // Add the discount from each voucher
                }else {
                    totalSale += v.getDiscountValue();
                }
            }
        }
        PaymentMethod paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod());
        long shipFee = 15000;
        long finalPrice = totalAmount-totalSale+shipFee;
        switch (paymentMethod) {
            case CASH :
                log.info("isOK");
                break;
            case ONLINE:
                long accAmount = account.getAmount();
                if (accAmount < finalPrice) throw new AppException(ErrorCode.NOT_ENOUGH_AMOUNT);
                account.setAmount(account.getAmount()-finalPrice);
                accountsRepository.save(account);
                break;
            case  QR:
                log.info("isOK2");
                break;
            default:
                break;
        }

        var shipInfo = ShipInfo.builder()
                .phone(request.getPhone())
                .wards(request.getWards())
                .name(request.getName())
                .district(request.getDistrict())
                .province(request.getProvince())
                .shipDetail(request.getShipDetail())
                .build();
        shipInfoRepository.save(shipInfo);
        var order = Order.builder()
                .account(account)
                .date(new Date())
                .totalAmount(totalAmount)
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.PENDING)
                .shipStatus(ShipStatus.PENDING)
                .vouchers(cart.getVouchers())
                .totalAmountBefore(finalPrice > 0 ? finalPrice : 0)
                .shipInfo(shipInfo)
                .build();

        Order savedOrder = orderRepository.save(order);
        List<OrderDetail> listODDT = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            var orderDetail = OrderDetail.builder()
                    .order(savedOrder)
                    .book(cartItem.getBook())
                    .quantity(cartItem.getQuantity())
                    .build();
            listODDT.add(orderDetail);
        }
        orderDetailRepository.saveAll(listODDT);
        cartItemRepository.deleteAll(cartItems);
        cartRepository.delete(cart);
        var notification = Notification.builder()
                .createAt(new Date())
                .isRead(false)
                .message("Cảm ơn bạn đã tin tưởng và mua hàng tại cửa hàng của chúng tôi! Đơn hàng của bạn đã được xác nhận và chúng tôi đang tiến hành chuẩn bị để giao đến bạn trong thời gian sớm nhất. Nếu bạn có bất kỳ thắc mắc nào, đừng ngần ngại liên hệ với chúng tôi. Chúc bạn có những trải nghiệm mua sắm tuyệt vời cùng chúng tôi!")
                .account(account)
                .build();
        notificationService.saveNotification(notification);
    }


    public ListBookPageResponse getAllOrder(int page, int size) {
        var account = securityService.getAccountByJWT();
        Pageable pageable = PageRequest.of(page, size);
        var listOrderPage = orderRepository.findAllByAccount_AccIDOrderByIDDesc(account.getAccID(), pageable);
        var listOrder = listOrderPage.getContent().stream().map(orderMapper::toResponse).toList();
        var response =  ListBookPageResponse.builder()
                .totalPages(listOrderPage.getTotalPages())
                .listOrder(listOrder)
                .currentPage(listOrderPage.getNumber())
                .build();
        response.getListOrder().stream().forEach(item -> {
            var ListOrderDetail =  orderDetailRepository.findByOrder_ID(item.getID());
            item.setFirstBook(ListOrderDetail.get(0).getBook());
            item.setTotalBook(ListOrderDetail.size());
        });
   return  response;
    }
    public OrderForAdminResponse getAllOrdersAdmin(int page, int size, int num) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        if(num >= 5) {
            var list = orderRepository.findAllByPaymentStatus(num == 5 ? PaymentStatus.PENDING : PaymentStatus.COMPLETED, pageable);
            return OrderForAdminResponse.builder()
                    .currentPage(list.getNumber())
                    .countOrderByShip(getShipStatusCounts())
                    .countOrderByPayment(getPaymentStatusCounts())
                    .totalPages(list.getTotalPages())
                    .listOrder(list.getContent())
                    .build();
        }
        if(num == 4 ) {
            var list = orderRepository.findAll(pageable);
            return OrderForAdminResponse.builder()
                    .currentPage(list.getNumber())
                    .countOrderByShip(getShipStatusCounts())
                    .countOrderByPayment(getPaymentStatusCounts())
                    .totalPages(list.getTotalPages())
                    .listOrder(list.getContent())
                    .build();
        }
        var list = orderRepository.findAllByShipStatus(ShipStatus.values()[num],pageable);
        return OrderForAdminResponse.builder()
                .currentPage(list.getNumber())
                .countOrderByShip(getShipStatusCounts())
                .countOrderByPayment(getPaymentStatusCounts())
                .totalPages(list.getTotalPages())
                .listOrder(list.getContent())
                .build();


    }
    private List<CountOrderByPayment> getPaymentStatusCounts() {
        List<Object[]> rawCounts = orderRepository.countOrdersByPaymentStatus();

        Map<PaymentStatus, Long> countsMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> (PaymentStatus) row[0],
                        row -> (Long) row[1]
                ));
        for (PaymentStatus status : PaymentStatus.values()) {
            countsMap.putIfAbsent(status, 0L);
        }

        return countsMap.entrySet().stream()
                .map(entry -> CountOrderByPayment.builder()
                        .paymentStatus(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<CountOrderByShip> getShipStatusCounts() {
        List<Object[]> rawCounts = orderRepository.countOrdersByShipStatus();

        Map<ShipStatus, Long> countsMap = rawCounts.stream()
                .collect(Collectors.toMap(
                        row -> (ShipStatus) row[0],
                        row -> (Long) row[1]
                ));

        for (ShipStatus status : ShipStatus.values()) {
            countsMap.putIfAbsent(status, 0L);
        }

        return countsMap.entrySet().stream()
                .map(entry -> CountOrderByShip.builder()
                        .shipStatus(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }


    public OrderDetailResponse getOrderDetailByID (int ID) {
//        var account = securityService.getAccountByJWT();
        var order = orderRepository.findById(ID).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
//        if(account != order.getAccount()) throw new AppException(ErrorCode.UNAUTHORIZED);
        var listOrderDetail = orderDetailRepository.findByOrder_ID(order.getID());
        var detailResponse = orderMapper.toOrderDetailResponse(order);
        List<BookInDetailResponse> listBookAndQuantity = new ArrayList<>();
        listOrderDetail.stream().forEach(item -> {
           var bookAndQuantity = BookInDetailResponse.builder()
                   .book(item.getBook())
                   .quantity(item.getQuantity())
                   .build();

            listBookAndQuantity.add(bookAndQuantity);
        });
        detailResponse.setListBook(listBookAndQuantity);
        detailResponse.setOrder(order);
        return detailResponse;

    }

    public void updateStatus(ChangeStatusOrderRequest request) {
        var order = orderRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if(request.getShipStatus() != null) {
            order.setShipStatus(ShipStatus.valueOf(request.getShipStatus()));
        }
        if(request.getPaymentStatus() != null) {
            order.setPaymentStatus(PaymentStatus.valueOf(request.getPaymentStatus()));
        }
        orderRepository.save(order);

    }

    public List<Order> searchOrder(String search) {
        return orderRepository.searchOrders(search);
    }
}
