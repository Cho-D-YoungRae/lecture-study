package hello.hellospring.domain;

public class Member {

    // long 이 아니라 Long 인 이유??
    // - 잦은 boxing, unboxing 이 일어나지 않도록?
    private Long id;
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
