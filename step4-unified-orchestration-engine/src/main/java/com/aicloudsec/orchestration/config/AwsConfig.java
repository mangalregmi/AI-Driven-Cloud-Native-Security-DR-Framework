package com.aicloudsec.orchestration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.route53.Route53Client;

@Configuration
public class AwsConfig {

    @Bean
    public Route53Client route53Client() {
        return Route53Client.create();
    }
}
