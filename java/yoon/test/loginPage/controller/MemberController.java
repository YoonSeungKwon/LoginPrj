package yoon.test.loginPage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoon.test.loginPage.service.MemberService;
import yoon.test.loginPage.vo.request.MemberLoginRequest;
import yoon.test.loginPage.vo.request.MemberRegisterRequest;
import yoon.test.loginPage.vo.response.MemberResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> joinMember(@RequestBody MemberRegisterRequest dto){
        if(dto == null)
            return ResponseEntity.badRequest().build();

        MemberResponse result = memberService.saveMember(dto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberLoginRequest dto){
        if(dto == null)
            return ResponseEntity.badRequest().build();

        MemberResponse result = memberService.loginMember(dto);

        return ResponseEntity.ok(result);
    }

}
