package com.fluance.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@EnableConfigurationProperties
@ConfigurationProperties(prefix="items")
@Data
public class TestConfiguration implements ItemApplicationConfiguration{
    private int maximumNumber;
}
