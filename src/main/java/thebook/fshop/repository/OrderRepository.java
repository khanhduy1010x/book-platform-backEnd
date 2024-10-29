package thebook.fshop.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Order;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByAccount_AccID(int accID);

    @Query("SELECT o FROM Order o WHERE " +
            "(:orderID IS NULL OR o.ID = :orderID) AND " +
            "(:accountID IS NULL OR o.account.accID = :accountID) AND " +
            "(:paymentStatus IS NULL OR o.paymentStatus = :paymentStatus) AND " +
            "(:paymentMethod IS NULL OR o.paymentMethod = :paymentMethod) AND " +
            "(:shipStatus IS NULL OR o.shipStatus = :shipStatus) AND " +
            "(:totalAmount IS NULL OR o.totalAmount = :totalAmount) AND " +
            "(COALESCE(:fromDate, o.date) <= o.date) AND " +
            "(COALESCE(:toDate, o.date) >= o.date)")
    List<Order> searchOrders(
            @Param("orderID") Integer orderID,
            @Param("accountID") Integer accountID,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("shipStatus") ShipStatus shipStatus,
            @Param("totalAmount") Long totalAmount,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );
}
