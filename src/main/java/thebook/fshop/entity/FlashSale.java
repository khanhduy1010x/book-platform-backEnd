package thebook.fshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "FlashSale")
public class FlashSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flashSaleID")
    int flashSaleID;

    @Column(name = "startTime", nullable = false)
    LocalDateTime startTime; // Thời gian bắt đầu

    @Column(name = "endTime", nullable = false)
    LocalDateTime endTime; // Thời gian kết thúc


}

