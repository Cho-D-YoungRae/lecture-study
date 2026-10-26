package com.example.jwt.jwtserver.auth;

import com.example.jwt.jwtserver.model.User;
import com.example.jwt.jwtserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// localhost:8080/login 요청 아래 서비스가 등장 -> SpringSecurity 기본 로그인 요청 주소
// 원래는 위 주소에서 동작해야되는데 저 주소는 formLogin 을 사용할 때이다. 우리는 현재 disable 되어있음
// 그렇기 때문에 직접 우리가 Filter 를 만들어 주어야 한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService.loadUserByUsername");
        User user = userRepository.findByUsername(username).orElse(null);
        return new PrincipalDetails(user);
    }
}
