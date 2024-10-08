package thebook.fshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import thebook.fshop.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBookByCategory_ID(int ID);

    @Query(
            "SELECT b FROM Book b WHERE  LOWER(b.bookName) LIKE %:query% OR  LOWER(b.author) LIKE %:query% OR  LOWER(b.category.cateName) LIKE %:query%")
    List<Book> findByBookNameAndAuthorAndMemberType(String query);

    Optional<Book> findByID(int bookID);
}
