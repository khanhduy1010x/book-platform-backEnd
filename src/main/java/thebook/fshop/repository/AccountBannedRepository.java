package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.AccountBanned;

import java.util.List;

@Repository
public interface AccountBannedRepository extends JpaRepository<AccountBanned, Integer> {
    List<AccountBanned> findByAccountAccID(int accID);
}
