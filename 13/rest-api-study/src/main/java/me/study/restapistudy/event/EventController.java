package me.study.restapistudy.event;

import lombok.RequiredArgsConstructor;
import me.study.restapistudy.index.IndexController;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@Valid @RequestBody EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        // 프로젝트구조가 간단하여 Controller 안에 있지만 이런 비즈니스 로직은 Service 로 위임해주는 것도 좋다.
        event.update();
        Event newEvent = eventRepository.save(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();

        /*
        EventResource eventResource = new EventResource(newEvent);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withSelfRel()); // 이런 자주 생성되는 것은 Resource 클래스 안에 넣어주어도 된다.
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        */
        EntityModel<Event> eventResource = EntityModel.<Event>of(newEvent,
                selfLinkBuilder.withSelfRel(),
                selfLinkBuilder.withRel("query-events"),
                selfLinkBuilder.withRel("update-event"),
                Link.of("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    // ErrorSerializer 에 의해서 현재 Errors 가 serialize 되는데 EntityModel.content 가 배열일 수 없어서 일단 아래 메소드 보류
    private ResponseEntity<EntityModel<Errors>> badRequest(Errors errors) {
        return ResponseEntity.badRequest()
                .body(EntityModel.of(
                        errors,
                        linkTo(IndexController.class).withRel("index")));
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page, e ->
                EntityModel.of(e, linkTo(EventController.class).slash(e.getId()).withSelfRel()));
        pagedModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("{id}")
    public ResponseEntity getEvent(@PathVariable long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        return ResponseEntity.ok(EntityModel.of(
                event,
                linkTo(EventController.class).slash(event.getId()).withSelfRel(),
                Link.of("/docs/index.html#resources-events-list").withRel("profile")
        ));
    }

    @PutMapping("{id}")
    public ResponseEntity updateEvent(
            @PathVariable long id,
            @RequestBody @Valid EventDto eventDto, Errors errors) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event existingEvent = optionalEvent.get();
        modelMapper.map(eventDto, existingEvent);
        // Service 가 없어서 현재 트랜잭션 안에 있지 않으므로 이렇게 처리
        Event savedEvent = eventRepository.save(existingEvent);

        EntityModel<Event> entityModel = EntityModel.of(savedEvent,
                linkTo(EventController.class).slash(savedEvent.getId()).withSelfRel(),
                Link.of("/docs/index.html#resources-events-update").withRel("profile")
        );
        return ResponseEntity.ok(entityModel);
    }
}
