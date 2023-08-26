package ru.nikitavov.soup.web.message.crud;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionResponse {
    private Long id;
    private String code;
    private String field;

    public ActionResponse(ActionResultCrud action) {
        id = action.getId();
        code = action.getCode();
        field = action.getField();
    }

    public static ActionResponse create(ActionResultCrud action) {
        return new ActionResponse(action);
    }
}
