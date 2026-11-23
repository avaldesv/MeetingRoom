package com.meetingroom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.demo-user")
public record DemoUserProperties(
        String username,
        String password
) {
}
