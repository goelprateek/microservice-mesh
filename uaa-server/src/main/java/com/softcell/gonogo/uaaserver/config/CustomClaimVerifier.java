package com.softcell.gonogo.uaaserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;

import java.util.Map;

/**
 * This class provides the capability of verifying the claim(s)
 * contained in a JWT Claims Set, for example, expiration time (exp),
 * not before (nbf), issuer (iss), audience (aud), subject (sub), etc.
 *
 */
public class CustomClaimVerifier implements JwtClaimsSetVerifier{

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomClaimVerifier.class);

    @Override
    public void verify(Map<String, Object> claims) throws InvalidTokenException {

        if(LOGGER.isDebugEnabled())
            LOGGER.debug(" using custom claims verifier ");

        final String username = (String) claims.get("user_name");

        if ((username == null) || (username.length() == 0)) {
            throw new InvalidTokenException("user_name claim is empty");
        }

    }
}
