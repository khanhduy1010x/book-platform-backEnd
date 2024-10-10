package thebook.fshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.AddtoCartRequest;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Request.DeleteCartRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Cart;
import thebook.fshop.service.CartService;
import thebook.fshop.mapper.CartMapper;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // API to add a book to the cart
    @PostMapping("/add")
    public ApiResponse<?> addToCart(@RequestBody AddtoCartRequest request) {
        cartService.addToCart(request);
        return ApiResponse.builder().build(); // Removed message
    }

    // API to view the cart of the current account
    @GetMapping("/view")
    public ApiResponse<List<CartResponse>> viewCart() {
        Account account = cartService.getCurrentAccount();
        List<Cart> carts = cartService.viewCart(account);

        // Use CartMapper to map the cart list
        List<CartResponse> cartResponses = CartMapper.toCartResponseList(carts);

        return ApiResponse.<List<CartResponse>>builder()
                .result(cartResponses)
                .build(); // Removed message
    }

    // API to update the quantity of a book in the cart
    @PutMapping("/update")
    public ApiResponse<?> updateCart(@RequestBody UpdateCartRequest request) {
        cartService.updateCart(request);
        return ApiResponse.builder().build(); // Removed message
    }

    // API to delete a book from the cart
    @DeleteMapping("/delete/{bookID}")
    public ApiResponse<?> deleteFromCart(@PathVariable int bookID) {
        DeleteCartRequest request = DeleteCartRequest.builder()
                .bookID(bookID)
                .build();
        cartService.deleteFromCart(request);
        return ApiResponse.builder().build(); // Removed message
    }
}

