package yoon.test.loginPage.enums;

import lombok.Getter;

@Getter
public enum Role {

    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String key;

    Role(String key){
        this.key = key;
    }
}
