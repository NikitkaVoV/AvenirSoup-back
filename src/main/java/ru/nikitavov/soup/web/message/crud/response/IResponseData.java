package ru.nikitavov.soup.web.message.crud.response;

import java.io.Serializable;

public interface IResponseData<T> extends Serializable {
    T getResult();
}
