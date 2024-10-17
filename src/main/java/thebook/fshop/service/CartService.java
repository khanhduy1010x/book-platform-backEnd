package thebook.fshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thebook.fshop.DTO.Request.AddToCartRequest;
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
    // Add an item to the cart

    public void addToCart(AddToCartRequest request){

    var account =securityService.getAccountByJWT();
        // Check if the book is in inventory and has enough stock
        Inventory inventory = inventoryRepository.findByBook_ID(request.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_INVENTORY));

        if (inventory.getQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.OVER_QUANTITY);
        }
if(request.getQuantity() <= 0) throw new AppException(ErrorCode.INVALID_QUANTITY);
        // Find the cart for the user
        Optional<Cart> optionalCart = cartRepository.findByAccount_AccID(account.getAccID());
        Cart cart = optionalCart.orElseGet(() -> {
            // Create a new cart if one doesn't exist
            Cart newCart = new Cart();
            newCart.setAccount(account);
            cartRepository.save(newCart);
            return newCart;
        });

        // Find the book being added
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if the item already exists in the cart
        Optional<CartItem> existingItem = cartItemRepository.findByCart_IDAndBook_ID(cart.getID(), book.getID());

        if (existingItem.isPresent()) {

            // If the item exists, update the quantity
            CartItem item = existingItem.get();
            int quantity = item.getQuantity() + request.getQuantity();
            if (quantity > inventory.getQuantity()) throw new AppException(ErrorCode.OVER_QUANTITY);

            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            // If not, add a new item to the cart
            if (inventory.getQuantity() < request.getQuantity()) {
                throw new AppException(ErrorCode.OVER_QUANTITY);
            }
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }
            // View the cart

    }
        // View the cart
    public List<CartResponse> viewCart() {
        var account =securityService.getAccountByJWT();
        // Find the cart for the user
        Cart cart = cartRepository.findByAccount_AccID(account.getAccID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        // Return the cart items as CartResponse DTOs
        var listCart= cartItemRepository.findByCart_ID(cart.getID());
        for (CartItem i : listCart){
            if ( inventoryRepository.findByBook_ID(i.getBook().getID()).isPresent()){
                if(i.getQuantity() >  inventoryRepository.findByBook_ID(i.getBook().getID()).get().getQuantity()) {
                    i.setOutOfStock(true);
                    cartItemRepository.save(i);
                }

         }  }
        return cartItemRepository.findByCart_ID(cart.getID()).stream().map(cartMapper::toCartResponse).collect(Collectors.toList());

    }
}
