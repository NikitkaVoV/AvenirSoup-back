package ru.nikitavov.soup.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.soup.database.model.base.User;
import ru.nikitavov.soup.database.model.security.LinkSocialNetworkToken;

import java.util.Optional;

public interface LinkSocialNetworkTokenRepository extends JpaRepository<LinkSocialNetworkToken, Long> {
    void deleteByUser(User user);
    Optional<LinkSocialNetworkToken> findByToken(String token);
}