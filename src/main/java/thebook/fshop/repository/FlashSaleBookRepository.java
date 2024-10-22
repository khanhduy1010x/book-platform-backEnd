package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import thebook.fshop.entity.FlashSaleBook;

public interface FlashSaleBookRepository extends JpaRepository<FlashSaleBook, Long> {
}
