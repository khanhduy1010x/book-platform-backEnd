package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import thebook.fshop.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder_ID(int orderID);
}
