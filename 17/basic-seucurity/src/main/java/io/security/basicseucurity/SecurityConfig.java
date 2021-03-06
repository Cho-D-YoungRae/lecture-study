package io.security.basicseucurity;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Order(0)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .anyRequest().authenticated()
//                .anyRequest().permitAll()
        ;

        http
                .formLogin();

        /*
        http    // ????????? ?????? default ??? ?????? ?????? ??????
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
                .permitAll();   // ???????????? ?????? loginPage ??? ????????? ??????
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
        /*
        http
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenValiditySeconds(3600)
                .userDetailsService(userDetailsService);
        */
        /*
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true);
        */
        /*
        http
                .sessionManagement()
                .sessionFixation().changeSessionId();
        */

        // ??? ?????? authorizeRequests ????????? ???????????? ?????? -> ??? ???????????? ????????????
//        http
//                .authorizeRequests()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/user").hasRole("USER")
//                .antMatchers("/admin/pay").hasRole("ADMIN")
//                .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
//                .anyRequest().authenticated();
        /*
        http
                .formLogin()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(
                            HttpServletRequest request, HttpServletResponse response,
                            Authentication authentication) throws IOException, ServletException {
                        System.out.println("SecurityConfig.onAuthenticationSuccess");
                        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                        SavedRequest savedRequest = requestCache.getRequest(request, response);
                        String redirectUrl = savedRequest.getRedirectUrl();
                        response.sendRedirect(redirectUrl);
                    }
                })
                .and()
                .exceptionHandling()
//                .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                    @Override
//                    public void commence(
//                            HttpServletRequest request, HttpServletResponse response,
//                            AuthenticationException authException) throws IOException, ServletException {
//                        System.out.println("SecurityConfig.commence");
//                        response.sendRedirect("/login");  // spring security ??? ??????????????? ????????? ????????? X
//                    }
//                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(
                            HttpServletRequest request, HttpServletResponse response,
                            AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        System.out.println("SecurityConfig.handle");
                        response.sendRedirect("/denied");
                    }
                });
        */
        /*
        http
                .antMatcher("/admin/**")
                .authorizeRequests()
                .anyRequest().authenticated()

                .and()
                .httpBasic();
        */

        /*
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        */
    }
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("{noop}1111").roles("USER");
        auth.inMemoryAuthentication().withUser("sys").password("{noop}1111").roles("SYS", "USER");
        auth.inMemoryAuthentication().withUser("admin").password("{noop}1111").roles("ADMIN", "SYS", "USER");
    }
    */


}

/*
@Configuration
@Order(1)
class SecurityConfig2 extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll()

                .and()
                .formLogin();
    }
}
*/
