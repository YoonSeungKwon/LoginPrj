package yoon.test.loginPage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import yoon.test.loginPage.entity.Members;
import yoon.test.loginPage.enums.Provider;
import yoon.test.loginPage.enums.Role;
import yoon.test.loginPage.repository.MemberRepository;
import yoon.test.loginPage.vo.request.MemberLoginRequest;
import yoon.test.loginPage.vo.request.MemberRegisterRequest;
import yoon.test.loginPage.vo.response.MemberResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private MemberResponse toResponse(Members member){
        return new MemberResponse(member.getIdx(), member.getEmail(), member.getName(), member.getRegdate(), member.getRoleKey());
    }

    public MemberResponse saveMember(MemberRegisterRequest dto){

        Members member = Members.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .provider(Provider.None)
                .role(Role.USER)
                .refreshToken(null)
                .build();

        return toResponse(memberRepository.save(member));
    }

    public MemberResponse loginMember(MemberLoginRequest dto)throws UsernameNotFoundException, BadCredentialsException {
        String username = dto.getEmail();
        String password = dto.getPassword();

        Members member = memberRepository.findMembersByEmail(username);
        if(member == null)
            throw new UsernameNotFoundException(username);
        if(!passwordEncoder.matches(password, member.getPassword()))
            throw new BadCredentialsException(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRoleKey()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return toResponse(member);
    }

    public MemberResponse getInfo(){
        Members member = (Members)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(member == null)
            return null;
        return toResponse(member);
    }
}
