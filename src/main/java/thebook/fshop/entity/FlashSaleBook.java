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
@Table(name = "FlashSaleBook")
public class FlashSaleBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;

    @ManyToOne
    @JoinColumn(name = "flashSaleID")
    FlashSale flashSale;

    @ManyToOne
    @JoinColumn(name ="bookID")
    Book book;

    double salePrice;
    @Column(name = "isActive", nullable = false)
    boolean isActive; // Trạng thái của flash sale (đang hoạt động hoặc đã kết thúc)

    int  quantity;

}
