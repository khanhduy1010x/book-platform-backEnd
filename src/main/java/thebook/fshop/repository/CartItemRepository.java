package thebook.fshop.repository;

import org.springframework.data.repository.CrudRepository;
import thebook.fshop.entity.Cart;
import thebook.fshop.entity.CartItem;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
    List<CartItem> findByCart_ID(int cartID);
}
