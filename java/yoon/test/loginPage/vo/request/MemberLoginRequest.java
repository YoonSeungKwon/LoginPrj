package yoon.test.loginPage.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import yoon.test.loginPage.exception.valid.sequence.ValidationGroup;

@Getter
public class MemberLoginRequest {
    @Email(message = "FORMAT01", groups =ValidationGroup.EmailNotFormat.class)
    @NotBlank(message = "BLANK01", groups =ValidationGroup.EmailNotNull.class)
    private String email;

    @NotBlank(message = "BLANK02", groups =ValidationGroup.PasswordNotNull.class)
    private String password;

}
