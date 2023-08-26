package ru.nikitavov.soup.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.soup.database.model.security.RefreshToken;

public interface RefreshTokenMyRepository extends JpaRepository<RefreshToken, String> {
}