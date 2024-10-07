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
@Table(name = "BookReadHistories")
public class BookReadHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookRH_ID")
    int id;

    @ManyToOne
    @JoinColumn(name = "accID")
    Account account;

    @ManyToOne
    @JoinColumn(name = "bookID")
    Book book;
}
