package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRateUpdateRequest {
    int bookRateID;
    int rate;
    String comment;
}
