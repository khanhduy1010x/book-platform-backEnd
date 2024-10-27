package thebook.fshop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Account;
import thebook.fshop.helper.Role;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Integer> {
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
    Optional<Account> findByPhone(String phone);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByUsername(String username);

    // Method to find users by their type
    List<Account> findByRole(Role role);

    // Add method to support pagination
    Page<Account> findAll(Pageable pageable);
}
