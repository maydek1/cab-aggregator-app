package com.software.modsen.carstationservice.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Setter
@Getter
@Configuration
public class TokenConverterProperties {

    @Value("${keycloak.clientId}")
    private String resourceId;
    private String principalAttribute;

    public Optional<String> getPrincipalAttribute() {
        return Optional.ofNullable(principalAttribute);
    }


}