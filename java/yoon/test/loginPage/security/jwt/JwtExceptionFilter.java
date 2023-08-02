package yoon.test.loginPage.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import yoon.test.loginPage.enums.ErrorCode;
import yoon.test.loginPage.vo.response.ErrorResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            setErrorResponse(response, ex);
        }
    }

    public void setErrorResponse(HttpServletResponse response, JwtException ex) throws IOException {
        ErrorCode error;
        if(ex.getMessage().equals(ErrorCode.ACCESS_TOKEN_EXPIRED.getCode())) {
            error = ErrorCode.ACCESS_TOKEN_EXPIRED;
        }
        else {
            error = ErrorCode.REFRESH_TOKEN_EXPIRED;
        }
        ErrorResponse errorResponse = new ErrorResponse(error.getCode(), error.getMessage());
        response.setStatus(error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}