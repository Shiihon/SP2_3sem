package app.security.token;

import app.controllers.CountryController;
import app.security.dtos.UserDTO;
import app.security.exceptions.ApiException;
import app.util.Utils;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Purpose: To provide JWT-related operations
 * Author: Thomas Hartmann
 * This class implements the ITokenSecurity interface to provide JWT-related operations.
 */
public class TokenSecurity implements ITokenSecurity {
    private static final Logger log = LoggerFactory.getLogger(TokenSecurity.class);


    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserWithRolesFromToken(String token) throws ParseException {
        // Return a user with Set of roles as strings
        SignedJWT jwt = SignedJWT.parse(token);
        String roles = jwt.getJWTClaimsSet().getClaim("roles").toString();
        String username = jwt.getJWTClaimsSet().getClaim("username").toString();

        Set<String> rolesSet = Arrays
                .stream(roles.split(","))
                .collect(Collectors.toSet());
        return new UserDTO(username, rolesSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean tokenIsValid(String token, String secret) throws ParseException, TokenVerificationException {
        boolean verified = false;
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            verified = jwt.verify(new MACVerifier(secret));
        } catch (JOSEException e) {
            throw new TokenVerificationException("Could not verify token", e.getCause());
        }
        if (verified)
            return true;
        else
            return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean tokenNotExpired(String token) throws ParseException{
        if (timeToExpire(token) > 0)
            return true;
        else
            return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int timeToExpire(String token) throws ParseException{
        SignedJWT jwt = SignedJWT.parse(token);
        return (int) (jwt.getJWTClaimsSet().getExpirationTime().getTime() - new Date().getTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createToken(UserDTO user, String ISSUER, String TOKEN_EXPIRE_TIME, String SECRET_KEY) throws TokenCreationException {
        // https://codecurated.com/blog/introduction-to-jwt-jws-jwe-jwa-jwk/
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer(ISSUER)
                    .claim("username", user.getUsername())
                    .claim("roles", user.getRoles().stream().reduce((s1, s2) -> s1 + "," + s2).get())
                    .expirationTime(new Date(new Date().getTime() + Integer.parseInt(TOKEN_EXPIRE_TIME)))
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());

            JWSSigner signer = new MACSigner(SECRET_KEY);
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            jwsObject.sign(signer);
            return jwsObject.serialize();

        } catch (JOSEException e) {
            e.printStackTrace();
            throw new TokenCreationException("Could not create token", e);
        }
    }
}
