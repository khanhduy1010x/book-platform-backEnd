package thebook.fshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thebook.fshop.DTO.Request.AddToCartRequest;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.CartItem;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Inventory;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.mapper.CartMapper;
import thebook.fshop.repository.CartItemRepository;
import thebook.fshop.repository.CartRepository;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Transactional
public class CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    BookRepository bookRepository;
    InventoryRepository inventoryRepository;
    SecurityService securityService;
    CartMapper cartMapper;

    public void addToCart(AddToCartRequest request) {
        var account = securityService.getAccountByJWT();
        Inventory inventory = inventoryRepository.findByBook_ID(request.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_INVENTORY));

        if (inventory.getQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.OVER_QUANTITY);
        }

        Optional<Cart> optionalCart = cartRepository.findByAccount_AccID(account.getAccID());
        Cart cart = optionalCart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setAccount(account);
            cartRepository.save(newCart);
            return newCart;
        });

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        Optional<CartItem> existingItem = cartItemRepository.findByCart_IDAndBook_ID(cart.getID(), book.getID());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int quantity = item.getQuantity() + request.getQuantity();
            if (quantity > inventory.getQuantity()) throw new AppException(ErrorCode.OVER_QUANTITY);
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }
    }

    public List<CartResponse> viewCart() {
        var account = securityService.getAccountByJWT();
        Cart cart = cartRepository.findByAccount_AccID(account.getAccID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        var listCart = cartItemRepository.findByCart_ID(cart.getID());
        for (CartItem i : listCart) {
            Inventory inventory = inventoryRepository.findByBook_ID(i.getBook().getID()).orElse(null);
            if (inventory != null && i.getQuantity() > inventory.getQuantity()) {
                i.setOutOfStock(true);
                cartItemRepository.save(i);
            }
        }

        return cartItemRepository.findByCart_ID(cart.getID()).stream().map(cartMapper::toCartResponse).collect(Collectors.toList());
    }

    public void updateCart(UpdateCartRequest request) {
        // First, check if the cart exists
        cartRepository.findById(request.getCartId())
                .orElseThrow(() ->  new AppException(ErrorCode.NOT_FOUND));

        // Check if the cart contains the book with the given bookId
        CartItem item = cartItemRepository.findByCart_IDAndBook_ID(request.getCartId(), request.getBookId())
                .orElseThrow(() ->  new AppException(ErrorCode.NOT_FOUND));

        if (request.getQuantity() == 0) {
            // If the quantity is 0, remove the item from the cart
            cartItemRepository.delete(item);
        } else {
            // Otherwise, update the quantity
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }
    }


}
