package hello.hellospring.domain;

import javax.persistence.*;

// JPA가 관리하도
@Entity
public class Member {

    // long 이 아니라 Long 인 이유??
    // - 잦은 boxing, unboxing 이 일어나지 않도록?
    // DB가 PK 자동으로 생성하도록 -> IDENTITY
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // DB 의 column 이름이 name 이므로 그대로 하면 된다.
    // 만약 DB column 이름이 username 이라면?
    // -> @Column(name = "username") 추가
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
