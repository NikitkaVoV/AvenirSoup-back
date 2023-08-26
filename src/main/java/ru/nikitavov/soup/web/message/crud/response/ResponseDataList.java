package ru.nikitavov.soup.web.message.crud.response;

import java.util.List;

public class ResponseDataList<R> extends ResponseDataAbstract<List<R>>{
    public ResponseDataList(List<R> result) {
        super(result);
    }
}
