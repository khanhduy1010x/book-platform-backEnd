package thebook.fshop.DTO.Request;

import lombok.Data;

@Data
public class BanAccountRequest {
    private int accountId;
    private String message;
}
