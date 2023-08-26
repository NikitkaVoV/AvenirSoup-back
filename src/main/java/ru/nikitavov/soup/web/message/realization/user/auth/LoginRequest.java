package ru.nikitavov.soup.web.message.realization.user.auth;

import ru.nikitavov.soup.web.message.intefaces.IRequest;

public record LoginRequest(String email, String password) implements IRequest {

}
