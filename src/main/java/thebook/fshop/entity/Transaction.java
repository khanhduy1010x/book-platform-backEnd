package thebook.fshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.TransactionType;

import java.util.Date;

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
    @JoinColumn(name ="accID")
    Account account;
    Date time;
    long amount;
    String content;
    TransactionType transactionType;
}
