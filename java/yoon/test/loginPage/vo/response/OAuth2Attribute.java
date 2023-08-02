package yoon.test.loginPage.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import yoon.test.loginPage.enums.Provider;

import java.util.Map;

@Data
public class OAuth2Attribute {

    private String email;

    private String name;

    private Provider provider;

    private String attributeKey;

    private Map<String, Object> attributes;

    @Builder
    public OAuth2Attribute(String email, String name, Provider provider, String attributeKey, Map<String, Object> attributes){
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.attributeKey = attributeKey;
        this.attributes = attributes;
    }

}
