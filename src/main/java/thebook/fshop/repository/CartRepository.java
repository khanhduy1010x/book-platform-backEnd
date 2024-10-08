package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByAccount_AccIDAndBook_ID(int accID, int bookID);
    List<Cart> findByAccount(Account account);
}
