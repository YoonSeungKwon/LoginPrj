package yoon.test.loginPage.enums;

import lombok.Getter;

@Getter
public enum Provider {

    None("None"), Google("Google"), Naver("Naver"), Kakao("Kakao");

    private final String value;

    Provider(String value){
        this.value = value;
    }
}
