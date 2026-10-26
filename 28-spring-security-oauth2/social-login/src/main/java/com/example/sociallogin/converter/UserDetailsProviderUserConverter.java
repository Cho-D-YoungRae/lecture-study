package com.example.sociallogin.converter;

import com.example.sociallogin.entity.User;
import com.example.sociallogin.model.FormUser;
import com.example.sociallogin.model.ProviderUser;

public class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser>  {

    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {
        if (providerUserRequest.user() == null) {
            return null;
        }
        User user = providerUserRequest.user();
        return FormUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .authorities(user.getAuthorities())
                .provider("none")
                .build();
    }
}
