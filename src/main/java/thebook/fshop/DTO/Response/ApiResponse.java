package thebook.fshop.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "result"})
public class ApiResponse<T> {

    @Builder.Default
    int code = 1000;

    String message;

    T result;
}
