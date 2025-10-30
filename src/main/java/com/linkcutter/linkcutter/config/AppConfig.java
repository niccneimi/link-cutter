package com.linkcutter.linkcutter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${SHORT_LINK_ID_LENGTH}")
    private int shortLinkIdLength;

    @Value("${APP_DOMAIN_NAME}")
    private String appDomainName;

    @Bean
    public LinkServiceProperties linkServiceProperties() {
        return new LinkServiceProperties(shortLinkIdLength, appDomainName);
    }
}
