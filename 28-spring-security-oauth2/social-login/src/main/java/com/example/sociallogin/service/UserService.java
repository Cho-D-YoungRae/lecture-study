package com.example.sociallogin.service;

import com.example.sociallogin.entity.User;
import com.example.sociallogin.model.ProviderUser;
import com.example.sociallogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(String registrationId, ProviderUser providerUser) {
        User user = User.builder()
                .registrationId(registrationId)
                .id(providerUser.getId())
                .username(providerUser.getUsername())
                .password(providerUser.getPassword())
                .email(providerUser.getEmail())
                .authorities(providerUser.getAuthorities())
                .build();

        userRepository.save(user);
    }

    public boolean exists(String username) {
        return userRepository.findByUsername(username)
                .isPresent();
    }

    public User get(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}
