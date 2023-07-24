package yoon.test.loginPage.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //BLANK ERROR
    EMAIL_BLANK(400, "BLANK01", "이메일을 입력해 주세요."),
    PASSWORD_BLANK(400, "BLANK02", "비밀번호를 입력해 주세요."),
    USERNAME_BLANK(400, "BLANK03", "이름을 입력해 주세요."),

    //FORMAT_ERROR
    EMAIL_NOT_FORMAT(400, "FORMAT01", "이메일 형식이 아닙니다."),
    PASSWORD_NOT_FORMAT(400, "FORMAT02", "비밀번호를 ?~? 자리로 입력해 주세요."),
    NAME_NOT_FORMAT(400, "FORMAT03", "이름을 한글로 입력해 주세요."),

    //DUPLICATION_ERROR
    EMAIL_DUPLICATION(400, "EMAIL_DUPLICATION", "이미 존재하는 이메일입니다."),

    //AUTHORIZATION_ERROR
    AUTHORIZATION_INVALID1(403, "AUTHORIZATION_INVALID_01", "유저만 접근 가능합니다."),
    AUTHORIZATION_INVALID2(403, "AUTHORIZATION_INVALID_02", "관리자만 접급 가능합니다.");


    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
