package ru.nikitavov.soup.web.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    private final Cors cors = new Cors();

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
        private String tokenRefreshSecret;
        private long tokenRefreshExpirationMsec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public String getTokenRefreshSecret() {
            return tokenRefreshSecret;
        }

        public long getTokenRefreshExpirationMsec() {
            return tokenRefreshExpirationMsec;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }

        public void setTokenRefreshSecret(String tokenRefreshSecret) {
            this.tokenRefreshSecret = tokenRefreshSecret;
        }

        public void setTokenRefreshExpirationMsec(long tokenRefreshExpirationMsec) {
            this.tokenRefreshExpirationMsec = tokenRefreshExpirationMsec;
        }
    }

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public static final class Cors {
        private ArrayList<String> allowedOrigins;

        public ArrayList<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(ArrayList<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }

    public Cors getCors() {
        return cors;
    }
}
