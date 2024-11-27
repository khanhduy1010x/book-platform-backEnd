package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import thebook.fshop.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder_ID(int id);
    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.book.ID = :bookId")
    int findTotalQuantityByBookId(@Param("bookId") int bookId);

}
