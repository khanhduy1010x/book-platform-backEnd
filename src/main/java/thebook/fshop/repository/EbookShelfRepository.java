package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thebook.fshop.entity.Category;
import thebook.fshop.entity.EbookShelf;

import java.util.List;

public interface EbookShelfRepository extends JpaRepository<EbookShelf, Integer> {
    EbookShelf findByAccount_AccIDAndBook_ID(int accID,int bookID);

    List<EbookShelf> findByAccount_AccID(int accID);
}
