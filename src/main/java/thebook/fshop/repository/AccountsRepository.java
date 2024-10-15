package thebook.fshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.Account;
import thebook.fshop.entity.Book;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Integer> {
    boolean existsByPhone(String phone);

    Optional<Account> findByPhone(String phone);

    Optional<Account> findByEmail(String email);
    @Query("SELECT b FROM Account b WHERE "
            + "LOWER(REPLACE(b.fullName, ' ', '')) LIKE %:query% "
            + "OR LOWER(REPLACE(b.email, ' ', '')) LIKE %:query% "
            + "OR LOWER(REPLACE(b.phone, ' ', '')) LIKE %:query%")
    List<Account> findByAccountNameOrEmailOrPhone(String query);

}
