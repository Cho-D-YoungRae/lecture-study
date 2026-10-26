package com.example.sociallogin.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class FormUser implements ProviderUser {

    private String registrationId;

    private String id;

    private String username;

    private String password;

    private String email;

    private String provider;

    private List<? extends GrantedAuthority> authorities;

    @Override
    public String getPicture() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
}

