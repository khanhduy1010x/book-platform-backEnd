package thebook.fshop.configuration;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.exception.ErrorCode;

@Slf4j
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        var errorCode = ErrorCode.UNAUTHORIZED;
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}
