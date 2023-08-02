package yoon.test.loginPage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import yoon.test.loginPage.entity.Members;
import yoon.test.loginPage.enums.Provider;
import yoon.test.loginPage.enums.Role;
import yoon.test.loginPage.repository.MemberRepository;
import yoon.test.loginPage.vo.response.OAuth2Attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2CustomService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User user = service.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String attributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute attribute = of(registrationId, attributeName, user.getAttributes());

        Members member = saveOrUpdate(attribute);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRoleKey()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attribute.getAttributes(),
                attribute.getAttributeKey()
        );
    }

    public static OAuth2Attribute of(String registrationId, String attributeName, Map<String, Object> attributes){
        if(registrationId.equals("Kakao"))
            return ofKakao(attributeName, attributes);
        else if(registrationId.equals("Naver"))
            return ofNaver(attributeName, attributes);
        else
            return ofGoogle(attributeName, attributes);
    }

    public static OAuth2Attribute ofGoogle(String attributeName, Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .provider(Provider.Google)
                .attributeKey(attributeName)
                .attributes(attributes)
                .build();
    }

    public static OAuth2Attribute ofNaver(String attributeName, Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .provider(Provider.Google)
                .attributeKey(attributeName)
                .attributes(attributes)
                .build();
    }

    public static OAuth2Attribute ofKakao(String attributeName, Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .provider(Provider.Google)
                .attributeKey(attributeName)
                .attributes(attributes)
                .build();
    }

    public Members saveOrUpdate(OAuth2Attribute attribute){
        Members member = memberRepository.findByEmail(attribute.getEmail())
                .map(entity->update(entity, attribute))
                .orElse(saveOAuthMember(attribute));
        return memberRepository.save(member);
    }

    public Members update(Members member, OAuth2Attribute attribute){
        member.setName(attribute.getName());
        return member;
    }

    public Members saveOAuthMember(OAuth2Attribute attribute){
        return Members.builder()
                .email(attribute.getEmail())
                .name(attribute.getName())
                .password(null)
                .provider(attribute.getProvider())
                .role(Role.USER)
                .build();
    }
}
