package com.example.sociallogin.config;

import com.example.sociallogin.converter.*;
import com.example.sociallogin.model.ProviderUser;
import com.example.sociallogin.service.CustomOAuth2UserService;
import com.example.sociallogin.service.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/static/js/**", "/static/images/**", "/static/css/**", "/static/scss/**"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomOAuth2UserService customOAuth2UserService, CustomOidcUserService customOidcUserService
    ) throws Exception {
        http.authorizeHttpRequests(config -> config
                .requestMatchers(HttpMethod.GET, "/api/user").hasAnyRole("SCOPE_profile", "SCOPE_email", "OAUTH2_USER")
                .requestMatchers(HttpMethod.GET, "/api/oidc").hasAnyRole("SCOPE_openid")
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .anyRequest().authenticated()
        );
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(config -> config
                        .userService(customOAuth2UserService)
                        .oidcUserService(customOidcUserService)
                )
        );
        http.formLogin(config -> config
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .defaultSuccessUrl("/").permitAll()
        );
        http.exceptionHandling(config -> config
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        );
        http.logout(config -> config
                .logoutSuccessUrl("/")
        );

        return http.build();
    }

    /**
     * 구글의 경우 SCOPE 가 email, profile 이런식이 아니라, http 로 시작하는 긴 문장으로 오도록 되어있음.
     * 권한 매핑할 때 이 긴 문장을 필터링해서 email, profile 등으로 잘라내어 주어야함.
     */
    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }

    @Bean
    public DelegatingProviderUserConverter delegatingProviderUserConverter() {
        List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> converters = List.of(
                new UserDetailsProviderUserConverter(),
                new OAuth2GoogleProviderUserConverter(),
                new OAuth2NaverProviderUserConverter(),
                new OAuth2KakaoProviderUserConverter(),
                new OAuth2KakaoOidcProviderUserConverter()
        );
        return new DelegatingProviderUserConverter(converters);
    }
}
