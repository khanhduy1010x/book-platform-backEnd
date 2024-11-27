package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Author;
import thebook.fshop.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByName(String name);
    @Query("SELECT a FROM Author a " +
            "WHERE ("+"(LOWER(FUNCTION('unaccent', a.name)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%')))"+")OR " +
            "  (LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "  )" +
            ")")
    List<Author> searchAuthor(@Param("keyword") String keyword);
}
