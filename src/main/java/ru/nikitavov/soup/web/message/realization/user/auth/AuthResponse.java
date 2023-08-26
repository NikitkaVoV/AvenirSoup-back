package ru.nikitavov.soup.web.message.realization.user.auth;

import org.springframework.http.HttpStatus;
import ru.nikitavov.soup.web.message.intefaces.IResponse;

public record AuthResponse(String accessToken, String refreshToken, long expiry) implements IResponse {

    @Override
    public HttpStatus defaultStatus() {
        return HttpStatus.OK;
    }
}
