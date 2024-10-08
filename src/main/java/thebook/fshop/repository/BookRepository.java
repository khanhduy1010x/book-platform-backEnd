package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Category;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {


    List<Book> findBookByCategory_ID(int ID);
    @Query("SELECT b FROM Book b WHERE b.bookName LIKE %:query% OR b.author LIKE %:query% OR b.category.cateName LIKE %:query%")
    List<Book> findByBookNameAndAuthorAndMemberType(
          String query);
}
