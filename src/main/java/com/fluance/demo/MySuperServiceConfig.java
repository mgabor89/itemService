package com.fluance.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@Primary
@ConfigurationProperties(prefix="items")
@Data
public class MySuperServiceConfig implements ItemApplicationConfiguration{
    private int maximumNumber = 13;
}
