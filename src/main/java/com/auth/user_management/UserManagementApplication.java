package com.auth.user_management;

import com.auth.user_management.constants.AppSettingsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppSettingsConfig.class)
@SpringBootApplication
public class UserManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}
}
