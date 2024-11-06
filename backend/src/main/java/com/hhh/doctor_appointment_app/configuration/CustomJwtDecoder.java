package com.hhh.doctor_appointment_app.configuration;

import com.hhh.doctor_appointment_app.dto.request.AuthenticationRequest.IntrospectRequest;
import com.hhh.doctor_appointment_app.exception.UnauthenticatedException;
import com.hhh.doctor_appointment_app.service.AuthenticationService.Query.Introspect.IntrospectQuery;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${spring.jwt.signerKey}")
    private String signerKey;

    @Autowired
    private IntrospectQuery introspectQuery;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException, UnauthenticatedException {

        try {
            var response = introspectQuery.introspect(
                    IntrospectRequest.builder().token(token).build());

            if (!response.isValid()) throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }

    //Method to get email from token
    public String getEmailFromToken(String token) {
        Jwt jwt = decode(token); // Decode token
        return jwt.getClaim("sub"); // Get email from payload.sub
    }

}