package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import thebook.fshop.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {}
