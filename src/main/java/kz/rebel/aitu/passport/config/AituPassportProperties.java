package kz.rebel.aitu.passport.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aitu.passport")
public class AituPassportProperties {
    @Value("${aitu.passport.url}")
    private String url;
    @Value("${aitu.passport.client-id}")
    private String clientId;
    @Value("${aitu.passport.secret}")
    private String secret;
    @Value("${aitu.passport.redirect-uri}")
    private String redirectUri;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
