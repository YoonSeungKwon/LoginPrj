package yoon.test.loginPage.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import yoon.test.loginPage.security.jwt.JwtAuthenticationFilter;
import yoon.test.loginPage.security.jwt.JwtExceptionFilter;
import yoon.test.loginPage.security.jwt.JwtProvider;
import yoon.test.loginPage.service.OAuth2CustomService;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final OAuth2CustomService oAuth2CustomService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth->{
                    auth.requestMatchers("/api/v1/test/*").permitAll();
                    auth.requestMatchers("/api/v1/members/**", "/api/v1/members/check/*").permitAll();
                    auth.requestMatchers("/api/v1/auth/**").permitAll();
                    auth.requestMatchers("/api/v1/users/**").hasRole("USER");
                    auth.anyRequest().authenticated();
                })

                .oauth2Login(login->login.userInfoEndpoint(user->user.userService(oAuth2CustomService)))

                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of( "*"));
        configuration.setExposedHeaders(List.of("Authorization","X-Refresh-Token"));


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
