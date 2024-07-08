package com.auth.user_management.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.secret")
@Configuration
public class AppSettingsConfig {
    private String publicKey;
    private String privateKey;
}
