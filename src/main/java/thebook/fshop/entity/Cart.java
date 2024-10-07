package thebook.fshop.entity;

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

    @ManyToOne
    @JoinColumn(name = "bookID")
    Book book;

    int quantity;
}
