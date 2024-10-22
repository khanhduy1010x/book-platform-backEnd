package thebook.fshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thebook.fshop.DTO.Request.OrderRequest;
import thebook.fshop.DTO.Response.OrderResponse;
import thebook.fshop.entity.Order;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.CartItem;
import thebook.fshop.entity.OrderDetail;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.CartRepository;
import thebook.fshop.repository.CartItemRepository;
import thebook.fshop.repository.OrderDetailRepository;
import thebook.fshop.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderService {

     OrderRepository orderRepository;
     CartRepository cartRepository;
     OrderDetailRepository orderDetailRepository;
    CartItemRepository cartItemRepository;
    SecurityService securityService;

    @Transactional
    public void createOrderFromCart(OrderRequest request) {
        // Fetch the user's cart
        var account = securityService.getAccountByJWT();
        Cart cart = cartRepository.findByAccount_AccID(account.getAccID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findByCart_ID(cart.getID());

        if (cartItems.isEmpty()) {
           throw new AppException(ErrorCode.NOT_FOUND);        }

        long totalAmount = cartItems.stream()
                .mapToLong(cartItem -> cartItem.getBook().getPrice() * cartItem.getQuantity())
                .sum();

      var order = Order.builder()
              .account(account)
              .date(new Date())
              .totalAmount(totalAmount)
              .paymentMethod(request.getPaymentMethod())
              .paymentStatus(request.getPaymentStatus())
              .shipStatus(request.getShipStatus())
              .build();   // default to pending

        // Save the new order
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
        // Clear the cart after creating the order (optional)
        cartItemRepository.deleteAll(cartItems);
        cartRepository.delete(cart);

    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<OrderResponse> viewOrder() {
        var account = securityService.getAccountByJWT();

        List<Order> orders;
        // Nếu người dùng có quyền ADMIN thì lấy tất cả các đơn hàng
        if (securityService.hasRole("ROLE_ADMIN")) {
            orders = orderRepository.findAll(); // Lấy tất cả các đơn hàng
        } else {
            // Ngược lại thì chỉ lấy đơn hàng của tài khoản hiện tại
            orders = orderRepository.findByAccount_AccID(account.getAccID());
        }

        if (orders.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        // Convert danh sách orders sang OrderResponse
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = OrderResponse.builder()
                    .orderID(order.getID())
                    .accountID(order.getAccount().getAccID())
                    .date(order.getDate())
                    .paymentMethod(order.getPaymentMethod())
                    .paymentStatus(order.getPaymentStatus())
                    .shipStatus(order.getShipStatus())
                    .totalAmount(order.getTotalAmount())
                    .build();

            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }


    public OrderResponse viewOrderDetail(int orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_ID(orderID);

        // Convert order details to response format
        List<OrderResponse.BookItem> bookItems = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            OrderResponse.BookItem bookItem = OrderResponse.BookItem.builder()
                    .book(orderDetail.getBook())
                    .quantity(orderDetail.getQuantity())
                    .build();
            bookItems.add(bookItem);
        }

        // Create the full order response
        return OrderResponse.builder()
                .orderID(order.getID())
                .accountID(order.getAccount().getAccID())
                .orderedBooks(bookItems)
                .date(order.getDate())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .shipStatus(order.getShipStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    public Order getOrderById(int orderID) {
        return orderRepository.findById(orderID)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Order not found with ID: " + orderID));
    }

}
