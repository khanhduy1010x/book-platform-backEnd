package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.UserMemberShip;

@Repository
public interface UserMemberShipRepository extends JpaRepository<UserMemberShip, Integer> {
    UserMemberShip findByAccount_AccID(int accID);

}
