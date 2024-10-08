package thebook.fshop.service;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thebook.fshop.DTO.Request.AddtoCartRequest;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.CartRepository;
import thebook.fshop.repository.BookRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class CartService {

    CartRepository cartRepository;
    BookRepository bookRepository;  // Add this to fetch Book
    SecurityService securityService;

    // Function to add a book to the cart
    public void addToCart(AddtoCartRequest request) {
        // Get the account of the user making the request
        var account = securityService.getAccountByJWT();

        // Fetch the book using the bookID from the request
        log.info("Book ID la : {}",request.getBookID());
       var book = bookRepository.findByID(request.getBookID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        // Check if the book is already in the cart for the account
        Cart existingCart = cartRepository.findByAccount_AccIDAndBook_ID(account.getAccID(), book.getID());
        if (existingCart != null) {
            // If the book is already in the cart, update the quantity
            existingCart.setQuantity(existingCart.getQuantity() + request.getQuantity());
             cartRepository.save(existingCart);
        } else {
            // If the book is not in the cart, create a new cart item
            Cart newCart = Cart.builder()
                    .account(account)
                    .book(book)
                    .quantity(request.getQuantity())
                    .build();
             cartRepository.save(newCart);
        }
    }

    // Function to view the cart of a specific account
    public List<Cart> viewCart(Account account) {
        return cartRepository.findByAccount(account);
    }
}
