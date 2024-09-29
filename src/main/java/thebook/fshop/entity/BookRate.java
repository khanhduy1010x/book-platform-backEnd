package thebook.fshop.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.Rate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "BookRates")
public class BookRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;

    @ManyToOne
    @JoinColumn(name = "bookID")
    Book book;

    Rate rate;

    String comment;

}
