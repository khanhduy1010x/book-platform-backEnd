package thebook.fshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByAccount_AccIDAndBook_ID(int accID, int bookID);

    List<Cart> findByAccount_AccID(int accID);
}
