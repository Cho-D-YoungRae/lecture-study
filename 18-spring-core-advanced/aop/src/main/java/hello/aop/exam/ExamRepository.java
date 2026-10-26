package hello.aop.exam;

import hello.aop.exam.annotation.Retry;
import hello.aop.exam.annotation.Trace;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ExamRepository {

    private static final AtomicInteger seq = new AtomicInteger(0);

    /**
     * 5번에 1번 실패하는 요청
     */
    @Trace
    @Retry
    public String save(String itemId) {
        seq.addAndGet(1);
        if (seq.intValue() % 5 == 0) {
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}
