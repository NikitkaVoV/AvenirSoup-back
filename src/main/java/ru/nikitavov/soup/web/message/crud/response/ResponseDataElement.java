package ru.nikitavov.soup.web.message.crud.response;

public class ResponseDataElement<T> extends ResponseDataAbstract<T>{
    public ResponseDataElement(T result) {
        super(result);
    }
}
