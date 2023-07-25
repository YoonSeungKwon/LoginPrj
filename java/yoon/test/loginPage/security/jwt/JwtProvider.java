package yoon.test.loginPage.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import yoon.test.loginPage.entity.Members;
import yoon.test.loginPage.repository.MemberRepository;
import yoon.test.loginPage.vo.request.MemberLoginRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final MemberRepository memberRepository;
    private String SECRET_KEY = "abcdefgyoonseungkwonopqrstuvwxyz";
    private long exp_acc = 30 * 60 * 1000L;
    private long exp_ref = 3 * 60 * 60 * 1000L;

    public String createAccessToken(String email){

        Claims claims = Jwts.claims()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +exp_acc));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT_ACCESS_TOKEN")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String createRefreshToken(String email){
        Members member = memberRepository.findMembersByEmail(email);
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +exp_ref));

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT_REFRESH_TOKEN")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        member.setRefreshToken(token);
        memberRepository.save(member);

        return token;
    }

    public String getEmail(String token){
        return (String)Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("email");
    }

    public Authentication getAuthentication(String token){
        Members members =  memberRepository.findMembersByEmail(getEmail(token));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(members.getRoleKey()));
        return new UsernamePasswordAuthenticationToken(members, null, authorities);
    }

    public boolean validateToken(String token){
        try{
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request){
        String token  = request.getHeader("Authorization");
        if(StringUtils.hasText(token) && token.startsWith("Bearer"))
            return token.substring(7);
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request){
        String token  = request.getHeader("X-Refresh-Token");
        if(StringUtils.hasText(token) && token.startsWith("Bearer"))
            return token.substring(7);
        return null;
    }
}
