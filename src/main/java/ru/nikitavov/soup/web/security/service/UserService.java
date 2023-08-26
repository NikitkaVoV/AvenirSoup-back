package ru.nikitavov.soup.web.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.nikitavov.soup.database.model.base.LinkedSocialNetwork;
import ru.nikitavov.soup.database.model.base.User;
import ru.nikitavov.soup.database.repository.realisation.LinkedSocialNetworkRepository;
import ru.nikitavov.soup.database.repository.realisation.UserRepository;
import ru.nikitavov.soup.general.model.socuialnetwork.SocialNetworkType;
import ru.nikitavov.soup.web.security.data.JwtAuthenticationToken;
import ru.nikitavov.soup.web.security.data.UserPrincipal;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final LinkedSocialNetworkRepository linkedSocialNetworkRepository;

    public Optional<User> getUser(UserPrincipal principal) {
        return userRepository.findById(principal.getId());
    }

    public User registryUser(String email, String password) {
        User user = createEmptyUser();
        user.setEmail(email);
        user.setPassword(password);

        return userRepository.save(user);
    }

    public User registryUser(SocialNetworkType type, String id) {
        User user = createEmptyUser();
        user = userRepository.save(user);

        addSocialNetwork(type, id, user);

        return user;
    }

    public void addSocialNetwork(SocialNetworkType type, String id, User user) {
        LinkedSocialNetwork socialNetwork = LinkedSocialNetwork.builder().socialNetworkType(type).user(user)
                .userSocialNetworkId(id).build();
        linkedSocialNetworkRepository.save(socialNetwork);
    }

    public User createEmptyUser() {
        User user = new User();
        user.setUuid(UUID.randomUUID());

        return user;
    }

    public Optional<User> getActiveUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return userRepository.findById(jwtAuthenticationToken.getUserPrincipal().getId());
        }

        return Optional.empty();
    }
}
