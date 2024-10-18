package thebook.fshop.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Request.DeleteCartRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.Account;
import thebook.fshop.service.CartService;


import thebook.fshop.DTO.Request.AddToCartRequest;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Request.DeleteCartRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {

    CartService cartService;

    @PostMapping("/add")
    public ApiResponse<?> addToCart(@RequestBody AddToCartRequest request) {
        cartService.addToCart(request);
        return ApiResponse.builder()
                .message("Item added to cart successfully")
                .build();
    }

    @GetMapping("/view")
    public ApiResponse<List<CartResponse>> viewCart() {
        return ApiResponse.<List<CartResponse>>builder()
                .result(cartService.viewCart())
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<?> updateCart(@RequestBody UpdateCartRequest request) {
        cartService.updateCart(request);
        return ApiResponse.builder()
                .message("Cart updated successfully")
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> deleteFromCart(@RequestBody DeleteCartRequest request) {
        cartService.deleteFromCart(request);
        return ApiResponse.builder()
                .message("Item removed from cart successfully")
                .build();
    }

}


