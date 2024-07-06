package com.auth.user_management.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.secret")
@Configuration
public class AppSettingsConfig {
    private String publicKey;
    private String privateKey;
}
