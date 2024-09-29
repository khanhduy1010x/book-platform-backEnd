package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendSMSServer {
    String to;
    String content;

    @Builder.Default
    int sms_type = 6;

    String sender;
}
