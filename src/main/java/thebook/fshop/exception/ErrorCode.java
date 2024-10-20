package thebook.fshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_CODE(0000, "Invalid code", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_CODE(9999, "Uncategorized code", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_USERNAME(
            1001,
            "Username must not contain spaces or special characters and must be between 6 - 25 characters",
            HttpStatus.BAD_REQUEST),
    NULL_USERNAME(1002, "Username must not be null", HttpStatus.BAD_REQUEST),

    NULL_PASSWORD(1003, "Password must not be null", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(
            1004,
            "A strong password must be at least 8 characters long, including at least one uppercase letter, one lowercase letter, one digit, and one special character.",
            HttpStatus.BAD_REQUEST),
    INVALID_FULL_NAME(
            1005,
            "A full name contains only letters and spaces, with no numbers, special characters, or extra spaces.",
            HttpStatus.BAD_REQUEST),
    NULL_FULL_NAME(
            1006,
            "A valid full name contains only letters and spaces, with no numbers or special characters, and must not exceed 40 characters.",
            HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER(1007, "Invalid phone number", HttpStatus.BAD_REQUEST),
    NULL_PHONE_NUMBER(1008, "Phone number must not be null", HttpStatus.BAD_REQUEST),
    EXITS_USERNAME(1009, "Exits the username", HttpStatus.BAD_REQUEST),
    NOT_EXITS_ACCOUNT(1010, "Not exits the account", HttpStatus.NOT_FOUND),
    NOT_EXITS_USERNAME(1011, "Not exits the username", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1012, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ERROR_GENERATE_TOKEN(1013, "Error Generate token", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_PARSE_TOKEN(1014, "Error Parse token", HttpStatus.UNAUTHORIZED),
    MISSING_TOKEN(1015, "Token is missing", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1016, "Token has expired", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1017, "You have no permission", HttpStatus.FORBIDDEN),
    NOT_FOUND(1018, "Not found", HttpStatus.NOT_FOUND),
    ERROR_FILE(1019, "Error loading file", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ID(1020, "Invalid id", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1021, "Invalid token", HttpStatus.BAD_REQUEST),
    INVALID_ENUM(1022, "Invalid enum", HttpStatus.BAD_REQUEST),
    EXITS_PHONE(1023, "Exits the phone", HttpStatus.BAD_REQUEST),
    ERROR_SEND(1024, "Error sending", HttpStatus.INTERNAL_SERVER_ERROR),
    WAITING_TIME(1025, "Waiting time", HttpStatus.BAD_REQUEST),
    NULL_OTP(1026, "OTP must not be null", HttpStatus.BAD_REQUEST),
    EXPIRED_OTP(1027, "Expired OTP", HttpStatus.BAD_REQUEST),
    INVALID_OTP(1028, "Invalid OTP", HttpStatus.BAD_REQUEST),
    INVALID_AVATAR(1029, "Invalid Avatar", HttpStatus.BAD_REQUEST),
    INVALID_FILE_NULL(1030, "Invalid File", HttpStatus.BAD_REQUEST),
    NULL_AVATAR(1032, "Avatar must not be null", HttpStatus.BAD_REQUEST),
    NULL_BIRTH(1033, "Username must not be null", HttpStatus.BAD_REQUEST),
    INVALID_BANNER_NULL(1039, "Banner is not null or empty", HttpStatus.BAD_REQUEST),
    NULL_BANNER(1040, "Banner haven't in database", HttpStatus.BAD_REQUEST),
    INVALID_BANNER(1041, "Invalid file type ", HttpStatus.BAD_REQUEST),
    INVALID_NAME_NULL(1042, "Invalid name", HttpStatus.BAD_REQUEST),
    INVALID_NAME(1043, "Invalid name", HttpStatus.BAD_REQUEST),
    INVALID_BIRTH(1044, "Invalid birthDay", HttpStatus.BAD_REQUEST),
    INVALID_NEW_PASSWORD(1045, "Invalid birthDAY", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(1046, "Invalid password", HttpStatus.BAD_REQUEST),
    DUPLICATE_BOOK(1047, "Invalid book", HttpStatus.BAD_REQUEST),
    SERVER_ERROR(1048, "Error in server", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_SIZE(1049, "Invalid file size", HttpStatus.BAD_REQUEST),
    INVALID_FILE_TYPE(1050, "Invalid file type", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY(1051, "Invalid quantity", HttpStatus.BAD_REQUEST),

    ;
    ;

    private int code;
    private String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
