package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.Voucher;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query(value = "SELECT * FROM vouchers v WHERE unaccent(v.voucher_name) ILIKE unaccent(concat('%', :searchTerm, '%')) OR v.voucher_name ILIKE concat('%', :searchTerm, '%')", nativeQuery = true)
    List<Voucher> findByVoucherNameWithAndWithoutUnaccent(@Param("searchTerm") String searchTerm);

}
