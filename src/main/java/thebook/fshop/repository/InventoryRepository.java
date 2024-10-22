package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Inventory;
import thebook.fshop.entity.Book;

import java.util.List;
import java.util.Optional;


import thebook.fshop.entity.Book;
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    // Tìm Inventory dựa trên đối tượng Book
    Inventory findByBook(Book book);
    Optional <Inventory> findByBook_ID(int bookId);
    // Các phương thức CRUD khác sẽ được Spring Data JPA tự động tạo dựa trên JpaRepository


    @Query(
            "SELECT i FROM Inventory i WHERE  LOWER(i.book.bookName) LIKE %:query% OR  LOWER(i.book.author) LIKE %:query% OR  LOWER(i.book.category.cateName) LIKE '%:query%'")
    List<Inventory> findByBookNameAndAuthorAndMemberType(String query);

}

