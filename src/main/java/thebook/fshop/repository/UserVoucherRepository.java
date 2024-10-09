package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.UserVoucher;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Integer> {}
