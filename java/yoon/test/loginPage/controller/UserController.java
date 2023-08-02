package yoon.test.loginPage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoon.test.loginPage.service.MemberService;
import yoon.test.loginPage.vo.response.MemberResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(){
        MemberResponse result = memberService.getInfo();
        if(result == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }
}
