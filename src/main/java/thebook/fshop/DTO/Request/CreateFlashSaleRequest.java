package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFlashSaleRequest {
    LocalDateTime startTime;
    LocalDateTime endTime;
    List<BookSaleRequest> bookSaleList;

}
