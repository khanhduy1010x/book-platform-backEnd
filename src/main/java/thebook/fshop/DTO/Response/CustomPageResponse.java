package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomPageResponse<T> {
    int pageNumber;     // Số trang hiện tại
    int totalPages;     // Tổng số trang
    long totalElements; // Tổng số phần tử
    List<T> content;    // Nội dung của trang
}
