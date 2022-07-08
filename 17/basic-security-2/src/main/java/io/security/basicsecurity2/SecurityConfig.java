package io.security.basicsecurity2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 1-6) Remember Me 인증
    // 1-7) Remember Me 인증 필터 : RememberMeAuthenticationFilter
//    private final UserDetailsService userDetailsService;

    // 1-11) 권한설정과 표현식
    @Bean
    public UserDetailsManager defaultUser() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("1111")
                .roles("USER")
                .build();

        UserDetails sys = User.withDefaultPasswordEncoder()
                .username("sys")
                .password("1111")
                .roles("SYS")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("ADMIN")
                .password("1111")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, sys, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureAccess(http);
        return http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//
//                .and()
                .formLogin()

                .and()
                .build();
    }

    // 1-3 Form Login 인증
    private HttpSecurity formLogin(HttpSecurity http) throws Exception {
        return http
                .formLogin()
//                .loginPage("/loginPage")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .usernameParameter("userId")
                .passwordParameter("passwd")
                .loginProcessingUrl("/login-proc")
                .successHandler((request, response, authentication) -> {
                    log.info("authentication={}", authentication.getName());
                    response.sendRedirect("/");
                })
                .failureHandler((request, response, exception) -> {
                    log.error("exception", exception);
                    response.sendRedirect("/login");
                })
                .permitAll()    // 해당 로그인 페이지로는 접근이 가능하도록
                .and();
    }

    // 1-5) Logout 처리, LogoutFilter
    private HttpSecurity logout(HttpSecurity http) throws Exception {
        return http
                .logout()
                .logoutUrl("/logout")   // default
                .logoutSuccessUrl("/login") // default : login?logout
                .addLogoutHandler((request, response, authentication) -> request.getSession().invalidate())
                .logoutSuccessHandler((request, response, authentication) -> response.sendRedirect("/login"))
                .deleteCookies("remember-me")
                .and();
    }

    // 1-6) Remember Me 인증
    // 1-7) Remember Me 인증 필터 : RememberMeAuthenticationFilter
    private HttpSecurity rememberMe(HttpSecurity http) throws Exception {
        return http
                .rememberMe()
//                .rememberMeParameter("remember")
//                .tokenValiditySeconds(3600)
//                .userDetailsService(userDetailsService)
                .and();
    }

    // 1-9) 동시 세션 제어, 세션 고정 보호, 세션 정책
    // 1-10) 세션 제어 필터 : SessionManagementFilter, ConcurrentSessionFilter
    private HttpSecurity sessionManagement(HttpSecurity http) throws Exception {
        return http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)

                .and()
                .sessionFixation()
//                .none()
                .changeSessionId()  // default

                .and();
    }

    // 1-11) 권한설정과 표현식
    private HttpSecurity configureAccess(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/admin/pay").hasRole("ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "SYS")
                .anyRequest().authenticated()
                .and();
    }
}
