package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thebook.fshop.entity.Book;

public interface BooKReadHistoryRepository extends JpaRepository<Book, Integer> {
}
