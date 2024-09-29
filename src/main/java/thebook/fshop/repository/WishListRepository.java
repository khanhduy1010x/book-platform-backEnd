package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.WishList;

@Repository
public interface WishListRepository  extends JpaRepository<WishList, Integer> {
}
