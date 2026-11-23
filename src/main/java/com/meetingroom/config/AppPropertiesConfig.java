package com.meetingroom.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, DemoUserProperties.class})
public class AppPropertiesConfig {
}
