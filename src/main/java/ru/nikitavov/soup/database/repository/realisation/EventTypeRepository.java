package ru.nikitavov.soup.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.soup.database.model.common.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
}