package thebook.fshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.Book;
import thebook.fshop.entity.Category;
import thebook.fshop.helper.BookType;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBookByCategory_ID(int ID);

    @Query(
            "SELECT b FROM Book b WHERE " +
                    "LOWER(FUNCTION('unaccent', b.bookName)) ILIKE %:query% " +
                    "OR LOWER(b.bookName) ILIKE %:query% " +
                    "OR LOWER(FUNCTION('unaccent', b.author.name)) ILIKE %:query% " +
                    "OR LOWER(b.author.name) ILIKE %:query% " +
                    "OR LOWER(FUNCTION('unaccent', b.category.cateName)) ILIKE %:query% " +
                    "OR LOWER(b.category.cateName) ILIKE %:query%"
    )
    List<Book> findByBookNameAndAuthorAndCategory(String query);

    List<Book> findBookByCategory_IDAndBookTypeOrderByMemberTypeDesc(int cateID, BookType bookType);

    Optional<Book> findByID(int bookID);
    @Query("SELECT DISTINCT b.category FROM Book b WHERE b.bookType = :bookType")
    List<Category> findCateIdsByBookType(BookType bookType);
}
