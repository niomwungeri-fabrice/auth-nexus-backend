package io.lynx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "encryption")
@Data
public class EncryptionConfig {
    private String key;
} 