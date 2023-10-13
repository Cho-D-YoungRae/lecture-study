package com.example.resourceserver.signature;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.PrivateKey;

@Setter
public class RsaPublicKeySecuritySigner extends SecuritySigner {

    private PrivateKey privateKey;

    @Override
    public String getJwtToken(UserDetails user, JWK jwk) throws JOSEException {

        RSASSASigner jwsSigner = new RSASSASigner(privateKey);

        return super.getJwtTokenInternal(jwsSigner, user, jwk);
    }
}
