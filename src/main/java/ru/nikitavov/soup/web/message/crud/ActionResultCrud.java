package ru.nikitavov.soup.web.message.crud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActionResultCrud {
    private Long id;
    private String code;
    private String field;
}
