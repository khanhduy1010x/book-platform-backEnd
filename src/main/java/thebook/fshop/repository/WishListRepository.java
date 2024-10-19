package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.WishList;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findAllByAccount_AccID(int accID);  // Fetch all wishlist items for a given account

    WishList findByBook_IDAndAccount_AccID(int bookID, int accID);
}
