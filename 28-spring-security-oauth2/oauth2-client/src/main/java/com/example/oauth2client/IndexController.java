package com.example.oauth2client;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/client-registrations/{registrationId}")
    public ClientRegistration getClientRegistration(@PathVariable String registrationId) {
        return clientRegistrationRepository.findByRegistrationId(registrationId);
    }

    // Security 에서 해주는 것
    @GetMapping("/oauth/user")
    public OAuth2User user(String accessToken) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(), Instant.MAX);
        OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);
        OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = userService.loadUser(oAuth2UserRequest);
        return oAuth2User;
    }

    // Security 에서 해주는 것
    @GetMapping("/oidc/user")
    public OAuth2User user(String accessToken, String idToken) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(), Instant.MAX, Set.of("read"));

        Map<String, Object> idTokenClaims = Map.of(
                IdTokenClaimNames.ISS, "http://localhost:8080/realms/oauth2",
                IdTokenClaimNames.SUB, "OIDC",
                "preferred_username", "user"
        );
        OidcIdToken oidcIdToken = new OidcIdToken(idToken, Instant.now(), Instant.MAX, idTokenClaims);
        OidcUserRequest oAuth2UserRequest = new OidcUserRequest(clientRegistration, oAuth2AccessToken, oidcIdToken);
        OAuth2UserService<OidcUserRequest, OidcUser> userService = new OidcUserService();
        OAuth2User oAuth2User = userService.loadUser(oAuth2UserRequest);
        return oAuth2User;
    }
}
