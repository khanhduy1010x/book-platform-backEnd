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
@Table(name = "CartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemID")
    int ID;

    @ManyToOne
    @JoinColumn(name = "cartID")
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "bookID")
    Book book;

    int quantity;
    @Column(nullable = false)
    boolean isInsufficient;
}
