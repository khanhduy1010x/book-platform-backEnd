package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Book;
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
