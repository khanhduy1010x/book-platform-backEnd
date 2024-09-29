package thebook.fshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "UserVouchers")
public class UserVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;

    @ManyToOne
    @JoinColumn(name ="accID")
    Account Account;

    @ManyToOne
    @JoinColumn(name = "voucherID")
    Voucher Voucher;

    Date expiration_date;

    boolean isUsed;


}
