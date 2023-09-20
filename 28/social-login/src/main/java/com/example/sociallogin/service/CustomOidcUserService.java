package com.example.sociallogin.service;

import com.example.sociallogin.converter.ProviderUserConverter;
import com.example.sociallogin.converter.ProviderUserRequest;
import com.example.sociallogin.model.PrincipalUser;
import com.example.sociallogin.model.ProviderUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService
        extends AbstractOAuth2UserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    public CustomOidcUserService(
            UserService userService, ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(userService, providerUserConverter);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService = new OidcUserService();
        OidcUser oidcUser = oAuth2UserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);

        ProviderUser providerUser = providerUser(providerUserRequest);

        // 회원가입
        register(providerUser, userRequest);

        return new PrincipalUser(providerUser);
    }
}
