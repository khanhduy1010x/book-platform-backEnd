package thebook.fshop.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddToCartRequest;
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
    public ApiResponse<List<CartResponse>> viewCart(@RequestParam int userId) {
        return ApiResponse.<List<CartResponse>>builder()
                .result(cartService.viewCart(userId))
                .build();
    }
//
//    @PutMapping("/update")
//    public ApiResponse<?> updateCart(@RequestBody UpdateCartRequest request) {
//        cartService.updateCart(request);
//        return ApiResponse.builder()
//                .message("Cart updated successfully")
//                .build();
//    }
//
//    @DeleteMapping("/delete/{bookID}")
//    public ApiResponse<?> deleteFromCart(@PathVariable int bookID, @RequestParam int userId) {
//        cartService.deleteFromCart(bookID, userId);
//        return ApiResponse.builder()
//                .message("Item removed from cart successfully")
//                .build();
//    }
}
