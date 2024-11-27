package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.ChangeStatusOrderRequest;
import thebook.fshop.DTO.Request.OrderCreationRequest;
//import thebook.fshop.DTO.Request.OrderRequest;
//import thebook.fshop.DTO.Request.OrderFilterRequest;
import thebook.fshop.DTO.Response.*;
//import thebook.fshop.DTO.Response.OrderResponse;
import thebook.fshop.entity.Order;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;
import thebook.fshop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {

     OrderService orderService;

    // API to create an order from cart
    @PostMapping("/create-from-cart")
    public ApiResponse<?> createOrderFromCart(@RequestBody OrderCreationRequest orderRequest) {
        log.info(orderRequest.toString());
        orderService.createOrderFromCart(orderRequest);
        return ApiResponse.builder().build();
    }

    @GetMapping("/get-all-order")
    public ApiResponse<ListBookPageResponse> getAllOrder(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        var result = orderService.getAllOrder(page, size);
        return ApiResponse.<ListBookPageResponse>builder()
                .result(result)
                .build();
    }
    @GetMapping("/admin/get-all-order/{num}")
    public ApiResponse<OrderForAdminResponse> getAllOrderAdmin(
            @PathVariable int num,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        var result = orderService.getAllOrdersAdmin(page, size,num);
        return ApiResponse.<OrderForAdminResponse>builder()
                .result(result)
                .build();
    }
//    @GetMapping("/view")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ApiResponse<List<OrderResponse>> viewOrder() {
//        return ApiResponse.<List<OrderResponse>>builder()
//                .result(orderService.viewOrder())
//                .build();
//    }
//
//
    // API to view order details
    @GetMapping("/view/{orderID}")
    public ApiResponse<OrderDetailResponse> viewOrderDetail(@PathVariable int orderID) {
        return ApiResponse.<OrderDetailResponse>builder()
                .result(orderService.getOrderDetailByID(orderID))
                .build();
    }

    @PostMapping("/change-status")
    public ApiResponse<?> changeStatus(@RequestBody ChangeStatusOrderRequest request) {
        orderService.updateStatus(request);
        return ApiResponse.builder()
                .build();
    }

    @GetMapping("/search/{param}")
    public ApiResponse<List<Order>> searchOrder(@PathVariable String param) {
        return ApiResponse.<List<Order>>builder()
                .result(orderService.searchOrder(param))
                .build();
    }
}
