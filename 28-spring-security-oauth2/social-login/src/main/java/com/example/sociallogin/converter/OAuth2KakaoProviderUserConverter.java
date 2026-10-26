package com.example.sociallogin.converter;

import com.example.sociallogin.model.KakaoUser;
import com.example.sociallogin.model.ProviderUser;
import com.example.sociallogin.util.OAuth2Utils;

public class OAuth2KakaoProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser>  {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        if (!"kakao".equals(providerUserRequest.clientRegistration().getRegistrationId())) {
            return null;
        }
        return new KakaoUser(
                providerUserRequest.clientRegistration(),
                providerUserRequest.oAuth2User(),
                OAuth2Utils.getOtherAttributes(
                        providerUserRequest.oAuth2User(), "kakao_account", "profile")
        );
    }
}
