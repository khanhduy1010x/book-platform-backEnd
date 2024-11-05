package thebook.fshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import thebook.fshop.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    // Find inventory by the book's ID (from the Book entity)
    Optional<Inventory> findByBook_ID(int bookId);
}
