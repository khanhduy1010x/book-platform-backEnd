package thebook.fshop.controller;


import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.DeleteCartRequest;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.service.CartService;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {

    CartService cartService;

//    @PostMapping("/add")
//    public ApiResponse<?> addToCart(@RequestBody AddtoCartRequest request) {
//        cartService.addToCart(request);
//        return ApiResponse.builder().build();
//    }

//    @GetMapping("/view")
//    public ApiResponse<List<CartResponse>> viewCart() {
//        return ApiResponse.<List<CartResponse>>builder()
//                .result(cartService.viewCart())
//                .build();
//    }
//
//    @PutMapping("/update")
//    public ApiResponse<?> updateCart(@RequestBody UpdateCartRequest request) {
//        cartService.updateCart(request);
//        return ApiResponse.builder().build();
//    }
//
//    @DeleteMapping("/delete/{bookID}")
//    public ApiResponse<?> deleteFromCart(@PathVariable int bookID) {
//        DeleteCartRequest request = DeleteCartRequest.builder().bookID(bookID).build();
//        cartService.deleteFromCart(request);
//        return ApiResponse.builder().build();
//    }
}

