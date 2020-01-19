package com.bluestone.holidayservice.configuration;

import com.bluestone.holidayservice.web.RestTemplateErrorHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(HolidayApiProperties.class)
public class HolidayServiceConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate holidaysApiRestTemplate(
            final RestTemplateBuilder builder, final HolidayApiProperties properties
    ) {
        return builder
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }
}
