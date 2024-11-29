package thebook.fshop.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.BookType;
import thebook.fshop.helper.EbookType;
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

    @Nullable
    Boolean isVisible;

    @ManyToOne
            @JoinColumn(name ="authorID")
    Author author;

    long price;

    @Enumerated(EnumType.STRING)
    EbookType ebookType;

    @Enumerated(EnumType.STRING)
    MemberType memberType;

    String url;
    String coverImage;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    BookType bookType;
}
