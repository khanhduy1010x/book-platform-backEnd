package thebook.fshop.repository;

import thebook.fshop.entity.User;  // Ensure this import is correct
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserType(String userType);
}

