package thebook.fshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.Account;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Integer> {
    boolean existsByPhone(String phone);

    Optional<Account> findByPhone(String phone);

    Optional<Account> findByEmail(String email);
}
