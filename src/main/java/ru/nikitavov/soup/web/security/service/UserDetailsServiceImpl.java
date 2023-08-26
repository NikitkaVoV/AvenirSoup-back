package ru.nikitavov.soup.web.security.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitavov.soup.database.model.base.User;
import ru.nikitavov.soup.database.repository.realisation.UserRepository;
import ru.nikitavov.soup.web.security.exception.ResourceNotFoundException;

import java.util.UUID;

/**
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPrincipalCreator principalCreator;
    
    @Override
    public UserDetails loadUserByUsername(String uuid)
            throws UsernameNotFoundException {
        System.out.println("uuid = " + uuid);
        User user = userRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with uuid : " + uuid));

        return principalCreator.create(user);
    }

    public UserDetails loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return principalCreator.create(user);
    }
}