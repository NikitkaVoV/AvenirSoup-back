package ru.nikitavov.soup.web.controller.rest.crud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nikitavov.soup.database.model.common.Event;
import ru.nikitavov.soup.database.repository.realisation.EventRepository;
import ru.nikitavov.soup.web.message.crud.ActionResponse;
import ru.nikitavov.soup.web.message.crud.ActionResultCrud;
import ru.nikitavov.soup.web.message.crud.request.EventRequest;
import ru.nikitavov.soup.web.message.crud.response.IResponseData;
import ru.nikitavov.soup.web.message.crud.response.ResponseDataElement;
import ru.nikitavov.soup.web.message.crud.response.ResponseDataList;

import java.util.Optional;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    
    @GetMapping("/")
    public ResponseEntity<IResponseData<?>> allItems() {
        return ResponseEntity.ok(new ResponseDataList<>(eventRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IResponseData<?>> getItem(@PathVariable int id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        return ResponseEntity.ok(new ResponseDataElement<>(optionalEvent.orElse(null)));
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<IResponseData<?>> postItem(@RequestBody EventRequest request) {
//        if (request.name().isEmpty()) {
//            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "name"))));
//        }
//        if (request.surname().isEmpty()) {
//            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "surname"))));
//        }
//        if (request.email().isEmpty()) {
//            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "email"))));
//        }
//
//        Integer id = eventRepository.save(Event.builder().email(request.email()).name(request.name())
//                .password(passwordEncoder.encode("changepassword"))
//                .surname(request.surname()).uuid(UUID.randomUUID()).build()).getId();
//
//        return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(id.longValue(), "SUCCESS", ""))));

        return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(null)));
    }

    @PutMapping("/")
    @Transactional
    public ResponseEntity<IResponseData<?>> putItem(@RequestBody EventRequest request) {
        return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(null)));
//        if (request.name().isEmpty()) {
//            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "name"))));
//        }
//        if (request.surname().isEmpty()) {
//            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "surname"))));
//        }
//        if (request.email().isEmpty()) {
//            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "FAIL", "email"))));
//        }
//
//
//        Optional<Event> findItem = eventRepository.findById(request.id());
//        if (findItem.isEmpty()) {
//            return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud((long) request.id(), "FAIL", "id"))));
//        }
//
//        Event event = findItem.get();
//        event.setName(request.name());
//        event.setSurname(request.surname());
//        event.setEmail(request.email());
//
//        Event newItem = eventRepository.save(event);
//        return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(newItem.getId().longValue(), "SUCCESS", ""))));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<IResponseData<?>> deleteItem(@PathVariable int id) {
        eventRepository.deleteById(id);
        return ResponseEntity.ok(new ResponseDataElement<>(ActionResponse.create(new ActionResultCrud(-1L, "SUCCESS", ""))));
    }
}
