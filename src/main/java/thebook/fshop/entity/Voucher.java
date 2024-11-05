package thebook.fshop.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.DiscountType;
import thebook.fshop.helper.MemberType;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucherID")
    int ID;

    String voucherName;

    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    boolean isPublic;

    @Enumerated(EnumType.STRING)
    MemberType memberType;

    long minCartValue;
    double discountValue;
    String voucherDescription;

    @ManyToMany
    @JoinTable(
            name = "VoucherCombinations",
            joinColumns = @JoinColumn(name = "voucherID_1"),
            inverseJoinColumns = @JoinColumn(name = "voucherID_2"))
    Set<Voucher> combinableVouchers;
}
