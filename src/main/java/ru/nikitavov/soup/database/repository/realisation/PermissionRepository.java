package ru.nikitavov.soup.database.repository.realisation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitavov.soup.database.model.security.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}