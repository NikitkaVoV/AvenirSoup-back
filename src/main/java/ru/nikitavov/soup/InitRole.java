package ru.nikitavov.soup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nikitavov.soup.database.model.base.Role;
import ru.nikitavov.soup.database.repository.realisation.RoleRepository;
import ru.nikitavov.soup.general.model.privilege.Roles;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InitRole {

    private final RoleRepository roleRepository;

    @Transactional
    public void init() {
        initRoles();
    }

    private void initRoles() {
        for (Roles role : Roles.values()) {
            Optional<Role> optionalRole = roleRepository.findByName(role.roleName());
            if (optionalRole.isEmpty()) {
                roleRepository.save(Role.builder().name(role.roleName()).build());
            }
        }
    }
}
