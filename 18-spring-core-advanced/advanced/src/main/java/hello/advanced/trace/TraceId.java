package hello.advanced.trace;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TraceId {

    private final String id;

    private final int  level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    private String createId() {
        // UUID 가 너무 길기 때문에 특정 길이만 사용
        // 어느정도 중복이 발생할 수 있으나 (거의 발생 X) 로그이므로 크게 문제 X
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
