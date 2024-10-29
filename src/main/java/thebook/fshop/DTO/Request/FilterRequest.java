package thebook.fshop.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.BookType;

import thebook.fshop.validation.EnumValue;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterRequest {
    @EnumValue(name = "type", enumClass = BookType.class)
    private String type;
    private String author;
    private int price;
    private int page ; // default page number
    private int size = 10; // default page size
}

