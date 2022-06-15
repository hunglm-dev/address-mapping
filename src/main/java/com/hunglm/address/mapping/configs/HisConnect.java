package com.hunglm.address.mapping.configs;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "app.his-connect")
@Data
@Slf4j
public class HisConnect {
    private String host;
    private Boolean usedAuth;

    @PostConstruct
    public void init(){
        log.info("HisConnect Config: {}", this);
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
