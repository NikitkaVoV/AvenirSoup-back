package ru.nikitavov.soup.web.data.action.realization;

import ru.nikitavov.soup.database.model.IEntity;
import ru.nikitavov.soup.web.message.intefaces.IResponseMessage;
import ru.nikitavov.soup.web.data.action.ActionResultType;

public class RepositoryActionResult<E extends IEntity<?>> extends ActionResult<E> {
    public RepositoryActionResult(E data, ActionResultType type, IResponseMessage... messages) {
        super(data, type, messages);
    }
}
