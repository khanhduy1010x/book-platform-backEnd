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
@Table(name = "Banners")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bannerID")
    int ID;
    String imageURL;
}
