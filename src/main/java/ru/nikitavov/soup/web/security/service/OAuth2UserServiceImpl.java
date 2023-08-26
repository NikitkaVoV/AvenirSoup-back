package ru.nikitavov.soup.web.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitavov.soup.database.model.base.LinkedSocialNetwork;
import ru.nikitavov.soup.database.model.base.User;
import ru.nikitavov.soup.database.model.security.LinkSocialNetworkToken;
import ru.nikitavov.soup.database.repository.realisation.LinkSocialNetworkTokenRepository;
import ru.nikitavov.soup.database.repository.realisation.LinkedSocialNetworkRepository;
import ru.nikitavov.soup.general.model.socuialnetwork.SocialNetworkType;
import ru.nikitavov.soup.web.security.exception.DisableUserException;
import ru.nikitavov.soup.web.security.exception.OAuth2AuthenticationProcessingException;
import ru.nikitavov.soup.web.security.oauth2.user.OAuth2UserInfo;
import ru.nikitavov.soup.web.security.oauth2.user.OAuth2UserInfoFactory;

import java.util.Optional;

import static ru.nikitavov.soup.web.security.service.HttpCookieAuthorizationRequestRepository.TOKEN_LINK_PARAM_COOKIE_NAME;

/**
 * Класс управления пользователями. Предназначен для кастомной загрузки пользователей
 * после авторизации через соц сети и получения
 */
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserService userService;
    private final LinkedSocialNetworkRepository linkedSocialNetworkRepository;
    private final UserPrincipalCreator principalCreator;
    private final LinkSocialNetworkTokenRepository linkSocialNetworkTokenRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
//             Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        Optional<LinkedSocialNetwork> socialNetwork = linkedSocialNetworkRepository
                .findByUserSocialNetworkIdAndSocialNetworkType(oAuth2UserInfo.getId(), oAuth2UserInfo.getType());

        User user;
        if (socialNetwork.isPresent()) {
            user = socialNetwork.get().getUser();
            checkUser(user);
        } else if (oAuth2UserRequest.getAdditionalParameters().containsKey(TOKEN_LINK_PARAM_COOKIE_NAME)) {
            user = linkNewSocialNetwork(oAuth2UserRequest, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return principalCreator.create(user, oAuth2User.getAttributes());
    }

    protected void checkUser(User user) {
        if (user.getDisabled()) {
            throw new DisableUserException("User is disabled");
        }
    }

    protected User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        SocialNetworkType type = SocialNetworkType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        return userService.registryUser(type, oAuth2UserInfo.getId());
    }

    protected User linkNewSocialNetwork(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String tokenLink = (String) oAuth2UserRequest.getAdditionalParameters().get(TOKEN_LINK_PARAM_COOKIE_NAME);
        Optional<LinkSocialNetworkToken> linkSocialNetworkToken = linkSocialNetworkTokenRepository.findByToken(tokenLink);

        if (linkSocialNetworkToken.isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Token not found");
        }

        User user = linkSocialNetworkToken.get().getUser();

        LinkedSocialNetwork linkedSocialNetwork = LinkedSocialNetwork.builder()
                .socialNetworkType(oAuth2UserInfo.getType())
                .userSocialNetworkId(oAuth2UserInfo.getId())
                .user(user)
                .build();

        linkSocialNetworkTokenRepository.delete(linkSocialNetworkToken.get());
        linkedSocialNetworkRepository.save(linkedSocialNetwork);

        return user;
    }
}
