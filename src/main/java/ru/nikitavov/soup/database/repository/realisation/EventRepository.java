package ru.nikitavov.soup.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.soup.database.model.common.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}