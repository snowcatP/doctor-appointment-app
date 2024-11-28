package com.hhh.doctor_appointment_app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableAsync
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS_POST = {
            "api/auth/signup",
            "api/auth/login",
            "api/auth/introspect",
            "api/auth/logout",
            "api/auth/forgot-password",
            "api/auth/reset-password",
            "api/auth/user/reset-password",
            "api/auth/refreshToken",
            "api/auth/register/user",
            "api/appointment/guest/create-appointment",
            "api/doctor/search",
            "api/appointment/search/reference-code"
    };

    private final String[] PUBLIC_ENDPOINTS_GET = {
            "api/doctor/get-doctors-for-booking",
            "api/specialty/all",
            "api/doctor/top-rating",
            "api/doctor/list-doctor",
            "api/doctor/detail/**",
            "api/feedback/list/doctor/**",
            "api/appointment/get-appointments-for-booking/*",
    };

    @Value("${spring.jwt.signerKey}")
    private String signerKey;

    @Value("${origins.host}")
    private String originHost;

    @Value("${origins.admin}")
    private String originAdmin;

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()
                                .requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS_POST).permitAll()
                                .anyRequest().authenticated()
                );

        httpSecurity
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();// Adjust if roles are in a different claim
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Prefix roles with "ROLE_"

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(originHost, originAdmin));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Cache-Control"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type"
        ));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

