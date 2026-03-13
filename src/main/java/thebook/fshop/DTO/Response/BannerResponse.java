package thebook.fshop.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.Rate;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerResponse {

    int id;
    String imageURL;
    BookInfo book;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookInfo {
        int id;
        String bookName;
        String coverImage;
        AuthorInfo author;
        List<RateInfo> bookRates;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthorInfo {
        int id;
        String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RateInfo {
        int id;
        Rate rate;
        String comment;
        Date date;
    }
}
