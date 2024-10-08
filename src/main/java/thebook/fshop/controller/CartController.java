package thebook.fshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.AddtoCartRequest;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Request.DeleteCartRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.Account;
import thebook.fshop.service.CartService;

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
        return ApiResponse.builder()
                .message("Book added to cart successfully")
                .build();
    }

    // API to view the cart of the current account
    @GetMapping("/view")
    public ResponseEntity<List<Cart>> viewCart() {
        Account account = cartService.getCurrentAccount();
        List<Cart> carts = cartService.viewCart(account);
        return ResponseEntity.ok(carts);
    }

    // API to update the quantity of a book in the cart
    @PutMapping("/update")
    public ApiResponse<?> updateCart(@RequestBody UpdateCartRequest request) {
        cartService.updateCart(request);
        return ApiResponse.builder()
                .message("Cart updated successfully")
                .build();
    }

    // API to delete a book from the cart
    @DeleteMapping("/delete/{bookID}")
    public ApiResponse<?> deleteFromCart(@PathVariable int bookID) {
        DeleteCartRequest request = DeleteCartRequest.builder()
                .bookID(bookID)
                .build();
        cartService.deleteFromCart(request);
        return ApiResponse.builder()
                .message("Book removed from cart successfully")
                .build();
    }
}
