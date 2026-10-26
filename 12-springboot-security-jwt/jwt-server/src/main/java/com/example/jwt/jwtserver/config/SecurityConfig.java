package com.example.jwt.jwtserver.config;

import com.example.jwt.jwtserver.jwt.JwtAuthenticationFilter;
import com.example.jwt.jwtserver.jwt.JwtAuthorizationFilter;
import com.example.jwt.jwtserver.filter.MyFilter1;
import com.example.jwt.jwtserver.filter.MyFilter4;
import com.example.jwt.jwtserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 내가 만든 필터는 Security Filter 가 아니므로 등록하려면
        // addFilterBefore, addFilterAfter 등으로 Security Filter 전 후로 등록
        // 혹은 Security Filter chain 에 등록하지 않고 따로 Filter 를 등록한다.
        http.addFilterBefore(new MyFilter1(), SecurityContextPersistenceFilter.class);
        http.addFilterAfter(new MyFilter4(), SecurityContextPersistenceFilter.class);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // local session 사용하지 않음
                .and()
                .addFilter(corsFilter)
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager : 로그인을 진행시킨다. -> WebSecurityConfigurerAdapter 가 갖고 있음
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeHttpRequests()
                .antMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .antMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll();
    }
}
