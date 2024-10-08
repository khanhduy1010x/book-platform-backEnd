package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.Rate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRateRequest {
    int bookID;  // ID của sách
    Rate rate;   // Đánh giá (số sao hoặc mức độ hài lòng)
    String comment;  // Bình luận của người dùng
}
