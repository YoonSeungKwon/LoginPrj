package yoon.test.loginPage.security.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import yoon.test.loginPage.enums.ErrorCode;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String acc_token = jwtProvider.resolveToken(request); //Access Token 헤더에서 가져오기
        if(acc_token!=null && !acc_token.equals("null")){
            if(jwtProvider.validateToken(acc_token)) {       //Access Token 검증
                Authentication authentication = jwtProvider.getAuthentication(acc_token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else{                                           //Access Token 기간 만료 Refresh Token 요청
                String ref_token = jwtProvider.resolveRefreshToken(request);
                if(ref_token == null)                       // 401에러를 보내서 Refresh Token 요청
                    throw new JwtException(ErrorCode.ACCESS_TOKEN_EXPIRED.getCode());
                if(jwtProvider.validateToken(ref_token)) {  // Refresh Token 검증
                    String new_token = jwtProvider.updateToken(ref_token);  //새로운 Access Token 생성
                    if (new_token != null) {
                        response.setHeader("Authorization", new_token);
                        Authentication authentication = jwtProvider.getAuthentication(new_token);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }else{                                       //Refresh Token 만료 예외처리
                    throw new JwtException(ErrorCode.REFRESH_TOKEN_EXPIRED.getCode());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
