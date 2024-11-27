package thebook.fshop.entity;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.PaymentMethod;
import thebook.fshop.helper.PaymentStatus;
import thebook.fshop.helper.ShipStatus;

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


    Date date;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    ShipStatus shipStatus;

    long totalAmount;
    long totalAmountBefore;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipInfoID")
    ShipInfo shipInfo;
    @ManyToMany
    @JoinTable(
            name = "OrderVouchers",
            joinColumns = @JoinColumn(name = "orderID"),
            inverseJoinColumns = @JoinColumn(name = "voucherID"))
    Set<Voucher> vouchers;
}
