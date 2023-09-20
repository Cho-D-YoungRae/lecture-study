package com.example.sociallogin.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KakaoUser extends OAuth2ProviderUser {

    public KakaoUser(ClientRegistration clientRegistration, OAuth2User oAuth2User, Attributes attributes) {
        super(clientRegistration, oAuth2User, Stream.of(
                        attributes.subAttributes().entrySet(),
                        attributes.otherAttributes().entrySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("nickname");
    }

    @Override
    public String getPicture() {
        return (String) getAttributes().get("profile_image_url");
    }
}
