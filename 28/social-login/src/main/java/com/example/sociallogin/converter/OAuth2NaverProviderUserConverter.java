package com.example.sociallogin.converter;

import com.example.sociallogin.model.NaverUser;
import com.example.sociallogin.model.ProviderUser;
import com.example.sociallogin.util.OAuth2Utils;

public class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser>  {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        if (!"naver".equals(providerUserRequest.clientRegistration().getRegistrationId())) {
            return null;
        }
        return new NaverUser(
                providerUserRequest.clientRegistration(),
                providerUserRequest.oAuth2User(),
                OAuth2Utils.getSubAttributes(providerUserRequest.oAuth2User(), "response")
        );
    }
}
