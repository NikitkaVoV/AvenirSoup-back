package ru.nikitavov.soup.web.security.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitavov.soup.database.model.base.Role;
import ru.nikitavov.soup.database.model.base.User;
import ru.nikitavov.soup.database.model.security.Permission;
import ru.nikitavov.soup.database.repository.realisation.UserRepository;
import ru.nikitavov.soup.web.security.data.UserPrincipal;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserPrincipalCreator {
    private final UserRepository userRepository;

    public UserPrincipalCreator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public UserPrincipal create(User user) {
        user = userRepository.findById(user.getId()).get();
        Set<Role> roles = user.getRoles();

        Set<String> permissions = roles.stream()
                .flatMap(role -> role.getPermissions().stream().map(Permission::getName))
                .collect(Collectors.toSet());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUuid(),
                permissions
        );
    }
}
