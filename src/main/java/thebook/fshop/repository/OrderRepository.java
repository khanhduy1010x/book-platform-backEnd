package thebook.fshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import thebook.fshop.DTO.Response.CountOrderByShip;
import thebook.fshop.entity.Order;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
        Page<Order> findAllByAccount_AccIDOrderByIDDesc(int accID, Pageable pageable);
        Page<Order> findAll(Pageable pageable);
        Page<Order> findAllByShipStatus(ShipStatus shipStatus, Pageable pageable);
        Page<Order> findAllByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);

        @Query("SELECT o.shipStatus, COUNT(o) " +
                "FROM Order o " +
                "GROUP BY o.shipStatus")
        List<Object[]> countOrdersByShipStatus();
        @Query("SELECT o.paymentStatus, COUNT(o) " +
                "FROM Order o " +
                "GROUP BY o.paymentStatus")
        List<Object[]> countOrdersByPaymentStatus();

        @Query("SELECT o FROM Order o " +
                "LEFT JOIN FETCH o.account a " +
                "LEFT JOIN FETCH o.shipInfo s " +
                "WHERE (" +
                "  (LOWER(FUNCTION('unaccent', a.username)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', a.email)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', a.fullName)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', a.phone)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', s.province)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', s.district)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', s.wards)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', s.shipDetail)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', s.name)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) OR " +
                "   LOWER(FUNCTION('unaccent', s.phone)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :keyword, '%')))" +
                "  ) OR " +
                "  (LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(a.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(a.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(s.province) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(s.district) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(s.wards) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(s.shipDetail) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "   LOWER(s.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
                "  )" +
                ")")
        List<Order> searchOrders(@Param("keyword") String keyword);
}
