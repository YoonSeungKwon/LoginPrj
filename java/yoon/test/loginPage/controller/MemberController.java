package yoon.test.loginPage.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yoon.test.loginPage.exception.valid.sequence.LoginValidationSequence;
import yoon.test.loginPage.exception.valid.sequence.RegisterValidationSequence;
import yoon.test.loginPage.security.jwt.JwtProvider;
import yoon.test.loginPage.service.MemberService;
import yoon.test.loginPage.vo.request.MemberLoginRequest;
import yoon.test.loginPage.vo.request.MemberRegisterRequest;
import yoon.test.loginPage.vo.response.MemberResponse;

import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    @GetMapping("/check/{email}")
    public ResponseEntity<Boolean> checkEmailDuplication(@PathVariable String email){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(memberService.checkEmailDuplication(email), headers, HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinMember(@RequestBody @Validated(RegisterValidationSequence.class) MemberRegisterRequest dto){

        MemberResponse result = memberService.saveMember(dto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody @Validated(LoginValidationSequence.class) MemberLoginRequest dto, HttpServletResponse response){

        MemberResponse result = memberService.loginMember(dto);

        String acc_token = jwtProvider.createAccessToken(dto.getEmail());
        String ref_token = jwtProvider.createRefreshToken(dto.getEmail());

        response.setHeader("Authorization", acc_token);
        response.setHeader("RefreshToken", ref_token);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
