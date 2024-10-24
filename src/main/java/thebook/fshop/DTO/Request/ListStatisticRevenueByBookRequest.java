package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.StatisticType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListStatisticRevenueByBookRequest {
    int month;
    int year;
    Date stDate;
    Date edDate;
    StatisticType statisticType;
}
