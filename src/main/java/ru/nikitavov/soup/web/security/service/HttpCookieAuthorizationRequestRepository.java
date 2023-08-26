package ru.nikitavov.soup.web.security.service;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;
import ru.nikitavov.soup.web.util.CookieUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Класс, который обеспечивает сохранение данных между началом и концом авторизации
 * По умолчанию Spring OAuth2 использует HttpSessionOAuth2AuthorizationRequestRepository для сохранения запроса авторизации.
 * Но поскольку наш сервис не имеет состояния, мы не можем сохранить его в сеансе.
 * Вместо этого мы сохраним запрос в файле cookie с кодировкой Base64.
 */
@Service
@RequiredArgsConstructor
public class HttpCookieAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String LINK_PARAM_COOKIE_NAME = "link";
    public static final String TOKEN_LINK_PARAM_COOKIE_NAME = "token_link";
    private static final int cookieExpireSeconds = 180;

    private final OAuth2SocialNetworkLinkHandler networkLinkHandler;

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        saveAuthorization(authorizationRequest, request, response);
    }

    public void saveAuthorization(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {

        authorizationRequest = addLinkSocialNetwork(authorizationRequest, request);

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }


    }

    public OAuth2AuthorizationRequest addLinkSocialNetwork(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
        if (StringUtils.isNotBlank(request.getParameter(LINK_PARAM_COOKIE_NAME))) {
            String token = networkLinkHandler.createTokenForAdd();
            if (StringUtils.isBlank(token)) return authorizationRequest;

            return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .additionalParameters(map -> map.put(TOKEN_LINK_PARAM_COOKIE_NAME, token)).build();
        }

        return authorizationRequest;
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, TOKEN_LINK_PARAM_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, LINK_PARAM_COOKIE_NAME);
    }
}
