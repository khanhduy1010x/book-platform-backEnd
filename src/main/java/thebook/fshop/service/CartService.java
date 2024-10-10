package thebook.fshop.service;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thebook.fshop.DTO.Request.AddtoCartRequest;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Request.DeleteCartRequest;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.mapper.CartMapper;
import thebook.fshop.repository.CartRepository;
import thebook.fshop.repository.BookRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class CartService {

    CartRepository cartRepository;
    BookRepository bookRepository;
    SecurityService securityService;
    CartMapper cartMapper;
    public void addToCart(AddtoCartRequest request) {
        var account = securityService.getAccountByJWT();
        Book book = bookRepository.findById(request.getBookID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        Cart existingCart = cartRepository.findByAccount_AccIDAndBook_ID(account.getAccID(), book.getID());
        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + request.getQuantity());
            cartRepository.save(existingCart);
        } else {
            Cart newCart = Cart.builder()
                    .account(account)
                    .book(book)
                    .quantity(request.getQuantity())
                    .build();
            cartRepository.save(newCart);
        }
    }

    public List<CartResponse> viewCart() {
        Account account = securityService.getAccountByJWT();
        return cartRepository.findByAccount_AccID(account.getAccID()).stream().map(cartMapper::toCartResponse).toList();
    }

    public void updateCart(UpdateCartRequest request) {
        Account account = securityService.getAccountByJWT();
        Book book = bookRepository.findById(request.getBookID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        Cart existingCart = cartRepository.findByAccount_AccIDAndBook_ID(account.getAccID(), book.getID());
        if (existingCart != null) {
            if (request.getQuantity() > 0) {
                existingCart.setQuantity(request.getQuantity());
                cartRepository.save(existingCart);
            } else {
                cartRepository.delete(existingCart);
            }
        } else {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
    }

    public void deleteFromCart(DeleteCartRequest request) {
        Account account = securityService.getAccountByJWT();
        Book book = bookRepository.findById(request.getBookID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        Cart existingCart = cartRepository.findByAccount_AccIDAndBook_ID(account.getAccID(), book.getID());
        if (existingCart != null) {
            cartRepository.delete(existingCart);
        } else {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
    }


}
