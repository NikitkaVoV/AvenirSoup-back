package ru.nikitavov.soup.web.security.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecuritySettingConfig {

    @Bean("clientRegistrationRepositoryCustom")
    public InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
        List<ClientRegistration> registrations = new ArrayList<>(OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties).values());
        registrations.add(vkClientRegistration());
        registrations.add(telegramClientRegistration());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration vkClientRegistration() {
        return ClientRegistration.withRegistrationId("vk")
                .clientId("51619094")
                .clientSecret("FICW27I3bARoAKajm7cl")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/oauth2/callback/{registrationId}")
                .scope("email")
                .authorizationUri("https://oauth.vk.com/authorize?v=5.131")
                .tokenUri("https://oauth.vk.com/access_token")
                .userInfoUri("https://api.vk.com/method/users.get?{user_id}&fields=bdate&v=5.131")
                .userNameAttributeName("response")
                .clientName("VK")
                .build();
    }

    private ClientRegistration telegramClientRegistration() {
        return ClientRegistration.withRegistrationId("telegram")
                .clientId("6263109518:AAHA8YRLBR55aJR1WZGCClpWtbR4O-6FO2c")
                .clientSecret("6263109518:AAHA8YRLBR55aJR1WZGCClpWtbR4O-6FO2c")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/oauth2/callback/{registrationId}")
                .scope("bot")
                .authorizationUri("https://telegram.org/oauth/authorize")
                .tokenUri("https://api.telegram.org/bot<telegram_bot_token>/getUpdates")
                .userInfoUri("https://api.telegram.org/bot<telegram_bot_token>/getMe")
                .userNameAttributeName("username")
                .clientName("Telegram")
                .build();
    }
}
