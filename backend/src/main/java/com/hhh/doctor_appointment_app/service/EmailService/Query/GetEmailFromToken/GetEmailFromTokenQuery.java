package com.hhh.doctor_appointment_app.service.EmailService.Query.GetEmailFromToken;

import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Service
public class GetEmailFromTokenQuery {

    @NonFinal
    @Value("${spring.jwt.signerKey}")
    protected String SIGNER_KEY;

    public String getEmailFromToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(Date.from(Instant.now()))) {
            throw new UnauthenticatedException("Reset password token expired");
        }

        var verified = signedJWT.verify(verifier);
        if (!verified) {
            throw new UnauthenticatedException("Reset password token verification failed");
        }

        return signedJWT.getJWTClaimsSet().getSubject();
    }
}
