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
@Table(name = "OrderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderDetailID")
    int ID;

    @ManyToOne
    @JoinColumn(name = "orderID")
    Order order;

    @ManyToOne
    @JoinColumn(name = "bookID")
    Book book;

    int quantity;

}
