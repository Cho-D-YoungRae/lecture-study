package io.security.basicseucurity;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated();
        http
                .formLogin();
        /*
        http    // 예제를 위해 default 와 다른 값을 구성
                .formLogin()
//                .loginPage("/loginPage")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .usernameParameter("userId")
                .passwordParameter("passwd")
                .loginProcessingUrl("/login_proc")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(
                            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                            throws IOException, ServletException {
                        System.out.println("authentication.getName() = " + authentication.getName());
                        response.sendRedirect("/");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(
                            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                            throws IOException, ServletException {
                        System.out.println("exception.getMessage() = " + exception.getMessage());
                        response.sendRedirect("/login");
                    }
                })
                .permitAll();   // 로그인을 위해 loginPage 의 접근을 허용
        */
        /*
        http
                .logout()
                .logoutUrl("/logout")   // default
//                .logoutSuccessUrl("/login")
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(
                            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        System.out.println("SecurityConfig.logout");
                        request.getSession().invalidate();
                    }
                })
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(
                            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                            throws IOException, ServletException {
                        System.out.println("SecurityConfig.onLogoutSuccess");
                        response.sendRedirect("/login");
                    }
                })
                .deleteCookies("remember-me");
        */
        http
                .rememberMe()
//                .rememberMeParameter("remember")
//                .tokenValiditySeconds(3600)
                .userDetailsService(userDetailsService);

    }
}
