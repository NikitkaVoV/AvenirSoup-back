package ru.nikitavov.soup.web.message.model.wrapper;

import ru.nikitavov.soup.web.message.intefaces.IResponseMessage;

import java.util.Collection;
import java.util.List;

public class CollectionMessageWrapper<R> extends MessageWrapper<Collection<R>> {
    public CollectionMessageWrapper(Collection<R> response, List<IResponseMessage> messages) {
        super(response, messages);
    }
}
