package hellojpa;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "team")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @Embedded
    private Period wordPeriod;

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "favorite_food",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Column(name = "food_name")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "address",
            joinColumns = @JoinColumn(name = "member_id")
    )
    private List<Address> addressHistory = new ArrayList<>();
}
