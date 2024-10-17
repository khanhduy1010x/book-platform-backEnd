package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInventoryRequest {
    Integer inventoryID;  // ID của bản ghi Inventory cần cập nhật
    int newQuantity;      // Số lượng mới
}
