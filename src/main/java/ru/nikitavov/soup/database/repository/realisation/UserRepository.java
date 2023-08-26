package ru.nikitavov.soup.database.repository.realisation;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitavov.soup.database.model.base.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);


    @Override
    List<User> findAll(Sort sort);
}