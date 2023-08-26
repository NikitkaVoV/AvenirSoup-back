package ru.nikitavov.soup.database.model;

import java.io.Serializable;

public interface IEntity<ID> extends Serializable {
    ID getId();
}
