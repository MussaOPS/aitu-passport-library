package kz.rebel.aitu.passport.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AituPassportProperties.class})
public class AituPassportConfiguration {

    public AituPassportConfiguration() {
    }
}
