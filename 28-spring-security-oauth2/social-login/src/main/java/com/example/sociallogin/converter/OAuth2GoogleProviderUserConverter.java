package com.example.sociallogin.converter;

import com.example.sociallogin.model.GoogleUser;
import com.example.sociallogin.model.ProviderUser;
import com.example.sociallogin.util.OAuth2Utils;

public class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser>  {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        if (!"google".equals(providerUserRequest.clientRegistration().getRegistrationId())) {
            return null;
        }
        return new GoogleUser(
                providerUserRequest.clientRegistration(),
                providerUserRequest.oAuth2User(),
                OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User())
        );
    }
}
