package thebook.fshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.AddtoCartRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // API to add a book to the cart
    @PostMapping("/add")
    public ApiResponse<?> addToCart(@RequestBody
            AddtoCartRequest request) {
         cartService.addToCart(request);
        return ApiResponse.builder().build();
    }

    // API to view the cart of a specific account
    @GetMapping("/view")
    public ResponseEntity<List<Cart>> viewCart(@RequestBody Account account) {
        List<Cart> carts = cartService.viewCart(account);
        return ResponseEntity.ok(carts);
    }
}

