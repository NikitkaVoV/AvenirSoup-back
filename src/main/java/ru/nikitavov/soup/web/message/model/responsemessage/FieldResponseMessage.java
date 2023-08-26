package ru.nikitavov.soup.web.message.model.responsemessage;


import ru.nikitavov.soup.web.message.intefaces.IResponseMessage;

public record FieldResponseMessage(String fieldName, String localizedFieldName, String message) implements IResponseMessage {
}
