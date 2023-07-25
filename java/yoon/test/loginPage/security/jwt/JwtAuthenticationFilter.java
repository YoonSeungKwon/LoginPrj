package yoon.test.loginPage.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import yoon.test.loginPage.enums.ErrorCode;
import yoon.test.loginPage.vo.response.ErrorResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String acc_token = jwtProvider.resolveToken(request);
        if(acc_token!=null){
            if(jwtProvider.validateToken(acc_token)) {
                Authentication authentication = jwtProvider.getAuthentication(acc_token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            String ref_token = jwtProvider.resolveRefreshToken(request);
            if(ref_token == null)
                throw new JwtException(acc_token);
            if(jwtProvider.validateToken(ref_token)){
                String email = jwtProvider.getEmail(acc_token);
                System.out.println(email);
            }
        }

        filterChain.doFilter(request, response);
    }

}
