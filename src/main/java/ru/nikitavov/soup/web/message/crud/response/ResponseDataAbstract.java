package ru.nikitavov.soup.web.message.crud.response;

public abstract class ResponseDataAbstract<T> implements IResponseData<T> {
    private final T result;

    public ResponseDataAbstract(T result) {
        this.result = result;
    }

    @Override
    public T getResult() {
        return result;
    }
}
