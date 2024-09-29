package thebook.fshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderID")
    int ID;

    @ManyToOne
    @JoinColumn(name = "accID")
    Account account;

    @ManyToOne
    @JoinColumn(name = "voucherID")
    Voucher voucher;

    Date date;

    PaymentMethod paymentMethod;

    PaymentStatus paymentStatus;

    ShipStatus shipStatus;

    long totalAmount;




}
