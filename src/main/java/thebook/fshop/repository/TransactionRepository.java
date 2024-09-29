package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thebook.fshop.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
