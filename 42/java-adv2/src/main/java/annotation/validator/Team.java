package annotation.validator;

public class Team {

    @NotEmpty(message = "팀 이름은 필수값입니다.")
    private String name;

    @Range(min = 1, max = 999, message = "멤버 수는 1과 999 사이여야 합니다.")
    private int memberCount;

    public Team(String name, int memberCount) {
        this.name = name;
        this.memberCount = memberCount;
    }

    public String getName() {
        return name;
    }

    public int getMemberCount() {
        return memberCount;
    }
}
