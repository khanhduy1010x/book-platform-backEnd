package thebook.fshop.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidToken {
    @Id
    String id;

    Date expiryDate;
}
