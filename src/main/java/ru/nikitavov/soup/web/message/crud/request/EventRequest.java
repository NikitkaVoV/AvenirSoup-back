package ru.nikitavov.soup.web.message.crud.request;

import java.time.LocalDateTime;

public record EventRequest(int id, String title, String description, String address, LocalDateTime startDate,
                           LocalDateTime endDate, int eventType) {
}
