package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.BookRate;

@Repository
public interface BookRateRepository  extends JpaRepository<BookRate, Integer> {
}
