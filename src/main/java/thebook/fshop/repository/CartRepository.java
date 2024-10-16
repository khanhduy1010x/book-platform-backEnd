package thebook.fshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.CartItem;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer> {
//    CartItem findByAccount_AccIDAndBook_ID(int accID, int bookID);
//
//    List<CartItem> findByAccount_AccID(int accID);
}
