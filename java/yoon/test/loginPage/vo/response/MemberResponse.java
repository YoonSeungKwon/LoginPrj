package yoon.test.loginPage.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private long idx;

    private String email;

    private String name;

    private LocalDateTime regdate;

    private String role;

}
