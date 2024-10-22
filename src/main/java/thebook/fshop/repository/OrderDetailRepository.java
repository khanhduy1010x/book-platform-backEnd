package thebook.fshop.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import thebook.fshop.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.book.ID = :bookId")
    int findTotalQuantityByBookId(@Param("bookId") int bookId);
}
