package thebook.fshop.entity;

import java.lang.reflect.Method;
import java.util.Date;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.MethodType;
import thebook.fshop.helper.TransactionType;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionID")
    int ID;

    @ManyToOne
    @JoinColumn(name = "accID")
    Account account;

    Date time;
    long beforeAmount;
    long afterAmount;
    String content;

    @Enumerated(EnumType.STRING)
    MethodType methodType;

    @Nullable
    long priceQR;

    @Enumerated(EnumType.STRING)
    TransactionType transactionType;
}
