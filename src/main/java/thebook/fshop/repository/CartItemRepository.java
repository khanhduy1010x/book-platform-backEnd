package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thebook.fshop.entity.CartItem;

import java.util.Optional;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCart_IDAndBook_ID(int cartId, int bookId);
    List<CartItem> findByCart_ID(int cartId);
}
