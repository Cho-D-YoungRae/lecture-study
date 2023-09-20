package com.example.sociallogin.service;

import com.example.sociallogin.converter.ProviderUserConverter;
import com.example.sociallogin.converter.ProviderUserRequest;
import com.example.sociallogin.model.ProviderUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

@Getter
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {

    private final UserService userService;

    private final ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    protected ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.convert(providerUserRequest);
    }

    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        if (userService.exists(providerUser.getUsername())) {
            log.info("User already exists >> userRequest={}", userRequest);
            return;
        }

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        userService.save(clientRegistration.getRegistrationId(), providerUser);
    }
}
