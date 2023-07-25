package yoon.test.loginPage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/infos")
    public ResponseEntity<Boolean> checkAuth(){
        String role = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        if(role.equals("[ROLE_ANONYMOUS]"))
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);

    }

}
