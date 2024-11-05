package thebook.fshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import thebook.fshop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCart_IDAndBook_ID(int cartId, int bookId);

    List<CartItem> findByCart_IDOrderByIDAsc(int cartId);

    List<CartItem> findByCart_ID(int cartId);



}
