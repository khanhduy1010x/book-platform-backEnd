package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.WishList;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    WishList findByBook_IDAndAccount_AccID(int id, int accID);
    List<WishList> findByAccount_AccID(int accID);
}
