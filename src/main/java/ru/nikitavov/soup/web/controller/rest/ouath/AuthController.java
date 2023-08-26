package ru.nikitavov.soup.web.controller.rest.ouath;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nikitavov.soup.database.model.base.User;
import ru.nikitavov.soup.database.repository.realisation.UserRepository;
import ru.nikitavov.soup.general.model.tuple.Tuple2;
import ru.nikitavov.soup.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.soup.web.message.realization.user.auth.AuthResponse;
import ru.nikitavov.soup.web.message.realization.user.auth.LoginRequest;
import ru.nikitavov.soup.web.message.realization.user.auth.SignUpRequest;
import ru.nikitavov.soup.web.security.data.UserPrincipal;
import ru.nikitavov.soup.web.security.service.TokenProvider;
import ru.nikitavov.soup.web.security.service.UserPrincipalCreator;
import ru.nikitavov.soup.web.security.exception.BadRequestException;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserPrincipalCreator principalCreator;

    @PostMapping("/login")
    public ResponseEntity<MessageWrapper<AuthResponse>> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.email());
        if (optionalUser.isEmpty()) return ResponseEntity.notFound().build();

        if (!passwordEncoder.matches(loginRequest.password(), optionalUser.get().getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        UserPrincipal principal = principalCreator.create(optionalUser.get());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
                null,
                principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Tuple2<String, Date> providerToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return ResponseEntity.ok(new MessageWrapper<>(new AuthResponse(providerToken.param1(), refreshToken, providerToken.param2().getTime())));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
//        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        UserPrincipal principal = principalCreator.create(result);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
                null,
                principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Tuple2<String, Date> providerToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return ResponseEntity.ok(new MessageWrapper<>(new AuthResponse(providerToken.param1(), refreshToken, providerToken.param2().getTime())));
    }

}
