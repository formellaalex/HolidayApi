package com.bluestone.holidayservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "holiday-service.holiday")
@Getter
@Setter
public class HolidayApiProperties {
    private String apiUrl;
    private String apiKey;
}
