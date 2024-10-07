package thebook.fshop.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
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
    long amount;
    String content;

    @Enumerated(EnumType.STRING)
    TransactionType transactionType;
}
