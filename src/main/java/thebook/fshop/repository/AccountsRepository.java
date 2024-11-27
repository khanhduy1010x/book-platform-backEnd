package thebook.fshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.Account;
import thebook.fshop.entity.Order;
import thebook.fshop.helper.MemberType;
import thebook.fshop.helper.Role;
import thebook.fshop.helper.ShipStatus;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Integer> {
    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    Optional<Account> findByPhone(String phone);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);
    // Method to find users by their type
    List<Account> findByRole(Role role);
    @Query("SELECT a.memberType, COUNT(a) " +
            "FROM Account a " +
            "GROUP BY a.memberType")
    List<Object[]> countMemberType();
    @Query("SELECT a.role, COUNT(a) " +
            "FROM Account a " +
            "GROUP BY a.role")
    List<Object[]> countRole();
    Page<Account> findAllByMemberType(MemberType memberType, Pageable pageable);
    Page<Account> findAllByRole(Role role, Pageable pageable);
    Page<Account> findAll( Pageable pageable);
    @Query("SELECT a.isBanned, COUNT(a) " +
            "FROM Account a " +
            "GROUP BY a.isBanned")
    List<Object[]> countByIsBannedStatus();
    Page<Account> findAllByIsBanned(boolean isBanned, Pageable pageable);

    @Query("SELECT a FROM Account a " +
            "WHERE (" +
            "  LOWER(FUNCTION('unaccent', a.username)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
            "  LOWER(FUNCTION('unaccent', a.email)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
            "  LOWER(FUNCTION('unaccent', a.fullName)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
            "  LOWER(FUNCTION('unaccent', a.phone)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
            "  LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "  LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "  LOWER(a.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "  LOWER(a.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            ")")
    List<Account> searchAccounts(@Param("keyword") String keyword);

}
