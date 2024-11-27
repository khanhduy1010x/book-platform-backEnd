package thebook.fshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import thebook.fshop.DTO.Request.AddToCartRequest;
import thebook.fshop.DTO.Request.DeleteCartRequest;
import thebook.fshop.DTO.Request.UpdateCartRequest;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.*;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.DiscountType;
import thebook.fshop.mapper.CartItemMapper;
import thebook.fshop.mapper.CartMapper;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.CartItemRepository;
import thebook.fshop.repository.CartRepository;
import thebook.fshop.repository.InventoryRepository;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Transactional
@Slf4j
public class CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    BookRepository bookRepository;
    InventoryRepository inventoryRepository;
    SecurityService securityService;
    CartMapper cartMapper;
    CartItemMapper cartItemMapper;

    public void addToCart(AddToCartRequest request) {
        var account = securityService.getAccountByJWT();
        Inventory inventory = inventoryRepository
                .findByBook_ID(request.getBookId())
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

        Book book =
                bookRepository.findById(request.getBookId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

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

    public CartResponse viewCart() {
        var account = securityService.getAccountByJWT();
        Cart cart = cartRepository
                .findByAccount_AccID(account.getAccID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        var listCart = cartItemRepository.findByCart_IDOrderByIDAsc(cart.getID());
        for (CartItem i : listCart) {
            Inventory inventory =
                    inventoryRepository.findByBook_ID(i.getBook().getID()).orElse(null);
            if (inventory != null && i.getQuantity() > inventory.getQuantity()) {
                i.setOutOfStock(true);
                cartItemRepository.save(i);
            }
        }

        var listCartItem =  cartItemRepository.findByCart_IDOrderByIDAsc(cart.getID()).stream().map(cartItemMapper::toResponse).toList();
        var totalPrice =calculateTotalInCart(cart.getID());
        var listVoucher = cart.getVouchers();
        long totalSale = 0;
        long shipFee = 15000;

        if (!listVoucher.isEmpty()) {
            for (Voucher v : listVoucher) {
                if (v.getDiscountType() == DiscountType.PERCENTAGE) {
                    totalSale += (v.getDiscountValue()/100) * totalPrice; // Add the discount from each voucher
                }else {
                    totalSale += v.getDiscountValue();
                }
            }
        }
        log.info("Account: {}", account);
        var afterPrice = totalPrice-totalSale + shipFee;
        return CartResponse.builder()
                .cartItems(listCartItem)
                .cartID(cart.getID())
                .account(account)
                .appliedVoucher(cart.getVouchers().stream().toList())
                .totalPriceAfterSale(afterPrice>=0 ? afterPrice  : 0)
                .totalSale(totalSale)
                .build();
    }

    public void updateCart(UpdateCartRequest request) {
        CartItem item = cartItemRepository
                .findById(request.getCartId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        if (request.getQuantity() == 0) {
            deleteFromCart(DeleteCartRequest.builder()
                    .cartItemID(item.getID())
                    .build());
        } else {
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
            var cart = item.getCart();
            updateCartTotalAndVouchers(cart);
        }
    }

    public void deleteFromCart(DeleteCartRequest request) {
        var cartItem = cartItemRepository.findById(request.getCartItemID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        cartItemRepository.delete(cartItem);

        var cart = cartItem.getCart();
        updateCartTotalAndVouchers(cart);
    }

    private void updateCartTotalAndVouchers(Cart cart) {
        var totalInCart = calculateTotalInCart(cart.getID());
        var listVoucher = cart.getVouchers();

        List<Voucher> vouchersToRemove = listVoucher.stream()
                .filter(v -> v.getMinCartValue() > totalInCart)
                .collect(Collectors.toList());

        listVoucher.removeAll(vouchersToRemove);
        cart.setVouchers(listVoucher);
        cartRepository.save(cart);
    }

    private long calculateTotalInCart(int cartId) {
        return cartItemRepository.findByCart_IDOrderByIDAsc(cartId).stream()
                .mapToLong(item -> item.getQuantity() * item.getBook().getPrice())
                .sum();
    }

}
