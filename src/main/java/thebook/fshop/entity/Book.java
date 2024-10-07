package thebook.fshop.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.MemberType;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookID")
    int ID;

    @ManyToOne
    @JoinColumn(name = "cateID")
    Category category;

    String bookName;
    String author;
    long price;

    @Enumerated(EnumType.STRING)
    MemberType memberType;

    String url;
    String coverImage;
    String description;
}
