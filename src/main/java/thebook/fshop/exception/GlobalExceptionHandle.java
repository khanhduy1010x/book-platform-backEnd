package thebook.fshop.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import thebook.fshop.DTO.Response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleUncaterizeException(Exception ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_CODE;
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode;
        String enumKey = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            errorCode = ErrorCode.INVALID_CODE;
        }
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ex.getErrorCode().getCode());
        apiResponse.setMessage(ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse> handleAuthorizationDeniedException(
            AuthorizationDeniedException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        var errorCode = ErrorCode.UNAUTHORIZED;
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(apiResponse.builder().code(errorCode.getCode()).build());
    }
}
