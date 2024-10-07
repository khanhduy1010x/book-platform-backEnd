package thebook.fshop.DTO.Response;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.TransactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponse {
    int ID;
    long amount;
    String content;
    TransactionType transactionType;
    Date time;
    String accID;
}
