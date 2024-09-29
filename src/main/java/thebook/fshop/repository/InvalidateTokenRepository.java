package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.InvalidToken;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidToken, String> {}
