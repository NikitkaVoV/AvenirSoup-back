package ru.nikitavov.soup.web.controller.rest.ouath;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nikitavov.soup.database.model.base.User;
import ru.nikitavov.soup.database.repository.realisation.RefreshTokenMyRepository;
import ru.nikitavov.soup.database.repository.realisation.UserRepository;
import ru.nikitavov.soup.web.message.crud.ActionResponse;
import ru.nikitavov.soup.web.message.crud.ActionResultCrud;
import ru.nikitavov.soup.web.message.crud.request.UserRequest;
import ru.nikitavov.soup.web.message.crud.response.IResponseData;
import ru.nikitavov.soup.web.message.crud.response.ResponseDataElement;
import ru.nikitavov.soup.web.message.crud.response.ResponseDataList;
import ru.nikitavov.soup.web.security.service.TokenProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider jwtTokenProvider;
    private final RefreshTokenMyRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<IResponseData<?>> allItems() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(new ResponseDataList<>(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IResponseData<?>> getItem(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return ResponseEntity.ok(new ResponseDataElement<>(optionalUser.orElse(null)));
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<IResponseData<?>> postItem(@RequestBody UserRequest request) {
        if (request.name().isEmpty()) {
            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "name"))));
        }
        if (request.surname().isEmpty()) {
            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "surname"))));
        }
        if (request.email().isEmpty()) {
            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "email"))));
        }

        Integer id = userRepository.save(User.builder().email(request.email()).name(request.name())
                .password(passwordEncoder.encode("changepassword"))
                .surname(request.surname()).uuid(UUID.randomUUID()).build()).getId();

        return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(id.longValue(), "SUCCESS", ""))));
    }

    @PutMapping("/")
    @Transactional
    public ResponseEntity<IResponseData<?>> putItem(@RequestBody UserRequest request) {
        if (request.name().isEmpty()) {
            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "name"))));
        }
        if (request.surname().isEmpty()) {
            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "surname"))));
        }
        if (request.email().isEmpty()) {
            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "email"))));
        }


        Optional<User> findItem = userRepository.findById(request.id());
        if (findItem.isEmpty()) {
            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud((long) request.id(), "FAIL", "id"))));
        }

        User user = findItem.get();
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setEmail(request.email());

        User newItem = userRepository.save(user);
        return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(newItem.getId().longValue(), "SUCCESS", ""))));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<IResponseData<?>> deleteItem(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "SUCCESS", ""))));
    }

}
