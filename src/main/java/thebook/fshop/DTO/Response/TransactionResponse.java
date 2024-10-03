package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.TransactionType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TransactionResponse {
    int ID; // ID của giao dịch
    long amount; // Số tiền giao dịch
    String content; // Nội dung giao dịch
    TransactionType transactionType; // Loại giao dịch
    Date time; // Thời gian giao dịch
    AccountResponse account; // Thông tin tài khoản liên quan
}
