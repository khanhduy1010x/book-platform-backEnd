package thebook.fshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.AddtoCartRequest;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Request.DeleteCartRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.Account;
import thebook.fshop.service.CartService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // API to add a book to the cart
    @PostMapping("/add")
    public ApiResponse<?> addToCart(@RequestBody AddtoCartRequest request) {
        cartService.addToCart(request);
        return ApiResponse.builder().message("Item added to cart successfully").build();
    }

    // API to view the cart of the current account
    @GetMapping("/view")
    public ApiResponse<List<CartResponse>> viewCart() {
        Account account = cartService.getCurrentAccount();
        List<Cart> carts = cartService.viewCart(account);

        // Map Cart entities to CartResponse objects
        List<CartResponse> cartResponses = carts.stream()
                .map(cart -> CartResponse.builder()
                        .cartID(cart.getID())
                        .accountID(cart.getAccount().getAccID()) // Using accID from Account entity
                        .accountFullName(cart.getAccount().getFullName()) // Using fullName from Account
                        .accountMemberType(cart.getAccount().getMemberType()) // Using memberType from Account
                        .bookID(cart.getBook().getID())
                        .bookName(cart.getBook().getBookName()) // Using bookName from Book entity
                        .bookAuthor(cart.getBook().getAuthor()) // Using author from Book entity
                        .bookPrice(cart.getBook().getPrice()) // Using price from Book entity
                        .bookMemberType(cart.getBook().getMemberType()) // Using memberType from Book entity
                        .bookCoverImage(cart.getBook().getCoverImage()) // Using coverImage from Book entity
                        .quantity(cart.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return ApiResponse.<List<CartResponse>>builder()
                .result(cartResponses)
                .build();
    }


    // API to update the quantity of a book in the cart
    @PutMapping("/update")
    public ApiResponse<?> updateCart(@RequestBody UpdateCartRequest request) {
        cartService.updateCart(request);
        return ApiResponse.builder().message("Cart updated successfully").build();
    }

    // API to delete a book from the cart
    @DeleteMapping("/delete/{bookID}")
    public ApiResponse<?> deleteFromCart(@PathVariable int bookID) {
        DeleteCartRequest request = DeleteCartRequest.builder()
                .bookID(bookID)
                .build();
        cartService.deleteFromCart(request);
        return ApiResponse.builder().message("Item removed from cart successfully").build();
    }
}
