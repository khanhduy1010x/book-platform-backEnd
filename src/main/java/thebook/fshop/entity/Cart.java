package thebook.fshop.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartID")
    int ID;

    @ManyToOne
    @JoinColumn(name = "accID")
    Account account;


    @ManyToMany
    @JoinTable(
            name = "CartVouchers",
            joinColumns = @JoinColumn(name = "cartID"),
            inverseJoinColumns = @JoinColumn(name = "voucherID"))
    Set<Voucher> vouchers;
}
