package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Inventory;
import thebook.fshop.entity.Book;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    // Tìm Inventory dựa trên đối tượng Book
    Inventory findByBook(Book book);

    // Các phương thức CRUD khác sẽ được Spring Data JPA tự động tạo dựa trên JpaRepository
}

