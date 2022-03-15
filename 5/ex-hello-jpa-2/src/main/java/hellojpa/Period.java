package hellojpa;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter(AccessLevel.PRIVATE)
public class Period {

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}