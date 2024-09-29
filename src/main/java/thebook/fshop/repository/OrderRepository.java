package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
