package com.example.resourceserver.signature;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.security.core.userdetails.UserDetails;

public class RsaSecuritySigner extends SecuritySigner {

    @Override
    public String getJwtToken(UserDetails user, JWK jwk) throws JOSEException {

        RSASSASigner jwsSigner = new RSASSASigner(jwk.toRSAKey());

        return super.getJwtTokenInternal(jwsSigner, user, jwk);
    }
}
