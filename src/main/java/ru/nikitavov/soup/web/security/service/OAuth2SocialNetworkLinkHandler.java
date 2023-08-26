package ru.nikitavov.soup.web.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nikitavov.soup.database.model.base.User;
import ru.nikitavov.soup.database.model.security.LinkSocialNetworkToken;
import ru.nikitavov.soup.database.repository.realisation.LinkSocialNetworkTokenRepository;
import ru.nikitavov.soup.web.security.util.KeyGenerator;
import ru.nikitavov.soup.web.security.util.TimeUtil;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2SocialNetworkLinkHandler {

    private final LinkSocialNetworkTokenRepository linkSocialNetworkTokenRepository;
    private final UserService userService;

    public String createTokenForAdd() {
        Optional<User> activeUser = userService.getActiveUser();
        if (activeUser.isEmpty()) {
            return "";
        }

        linkSocialNetworkTokenRepository.deleteByUser(activeUser.get());

        String token = KeyGenerator.string(64);
        Date expiryDate = TimeUtil.createExpiryDate(300000);

        linkSocialNetworkTokenRepository.save(LinkSocialNetworkToken.builder()
                .token(token)
                .user(activeUser.get())
                .expiryDate(expiryDate)
                .build());

        return token;
    }
}
