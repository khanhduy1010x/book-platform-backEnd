package thebook.fshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.OrderRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.OrderResponse;
import thebook.fshop.entity.CartItem;
import thebook.fshop.entity.Order;
import thebook.fshop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // API to create an order from cart
    @PostMapping("/create-from-cart")
    public ApiResponse<?> createOrderFromCart(@RequestBody OrderRequest orderRequest) {
     orderService.createOrderFromCart(orderRequest);
        return ApiResponse.builder().build();
    }

    @GetMapping("/view")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ApiResponse<List<OrderResponse>> viewOrder() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.viewOrder())
                .build();
    }


    // API to view order details
    @GetMapping("/view/{orderID}")
    public ApiResponse<OrderResponse> viewOrderDetail(@PathVariable int orderID) {
        Order order = orderService.getOrderById(orderID);
        // Sau đó bạn có thể chuyển đổi `Order` sang `OrderResponse`
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.viewOrderDetail(orderID))
                .build();
    }


}
