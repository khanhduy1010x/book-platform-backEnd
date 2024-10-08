package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.Rate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRateResponse {
    int id;           // ID của đánh giá
    int bookID;       // ID của sách
    Rate rate;        // Đánh giá
    String comment;   // Bình luận
}
