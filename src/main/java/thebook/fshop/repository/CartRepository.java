package thebook.fshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import thebook.fshop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByAccount_AccID(int userId);
}
