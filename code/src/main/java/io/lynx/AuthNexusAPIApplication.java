package io.lynx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import io.lynx.config.EncryptionConfig;

@SpringBootApplication
@EnableConfigurationProperties(EncryptionConfig.class)
public class AuthNexusAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthNexusAPIApplication.class, args);
	}
}
