package me.study.restapistudy.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/*

@AllArgsConstructor
@Getter
public class EventResource extends RepresentationModel<EventResource> {

    @JsonUnwrapped
    private Event event;
}
*/

public class EventResource extends EntityModel<Event> {

    public EventResource(Event content, Link... links) {
        super(content, Arrays.asList(links));
        // 이런 자주 생성되는 것은 Resource 클래스 안에 넣어주어도 된다.
        add(linkTo(EventController.class).slash(content.getId()).withSelfRel());
    }
}

