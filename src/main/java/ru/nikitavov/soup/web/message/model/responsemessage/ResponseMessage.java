package ru.nikitavov.soup.web.message.model.responsemessage;

import ru.nikitavov.soup.web.message.intefaces.IResponseMessage;

public record ResponseMessage(String message) implements IResponseMessage {
}
