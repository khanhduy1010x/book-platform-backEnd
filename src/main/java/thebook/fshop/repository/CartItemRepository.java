package thebook.fshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import thebook.fshop.entity.CartItem;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCart_IDAndBook_ID(int cartId, int bookId);
    Page<CartItem> findByCart_ID(int cartId, Pageable pageable);
}
