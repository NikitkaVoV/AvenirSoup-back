package ru.nikitavov.soup.web.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.util.UriComponentsBuilder;
import ru.nikitavov.soup.web.util.CookieUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.nikitavov.soup.web.security.service.HttpCookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Log4j2
public class RedirectUrl {

    private final String redirectUrl;
    private final ObjectMapper jsonConverter;
    private final Map<String, String> params = new HashMap<>();

    public RedirectUrl(HttpServletRequest request, ObjectMapper jsonConverter) {
        redirectUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));


        this.jsonConverter = jsonConverter;
    }

    public RedirectUrl addParam(String key, Object param, boolean toJson) {
        if (param == null) {
            params.put(key, null);
        } else if (param instanceof String) {
            params.put(key, (String) param);
        } else if (param instanceof Optional<?> optional) {
            Object o = optional.orElse(null);
            addParam(key, o, toJson);
        } else if (toJson) {
            try {
                String json = jsonConverter.writeValueAsString(param);
                params.put(key, json);
            } catch (JsonProcessingException e) {
                log.error("Error when converting an object to json", e);
            }
        } else {
            params.put(key, param.toString());
        }

        return this;
    }

    public String toStringUrl() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUrl);
        params.forEach(builder::queryParam);
        return builder.build().toUriString();
    }
}
