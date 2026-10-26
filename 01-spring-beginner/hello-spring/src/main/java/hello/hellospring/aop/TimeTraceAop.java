package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// @Aspect -> AOP 로 사용할 수 있도록
// 스프링 빈에 등록해야 한다 -> 정형화된 것이 아니므로 컴포넌트 스캔 보다는 코드로 등록하는 것이 더 좋아보임
@Aspect
@Component
public class TimeTraceAop {

    // 메뉴얼 보고 따라가면 된다.
    @Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        // 어떤 메소드가 실행되는지 확인하기 위해
        System.out.println("START: " + joinPoint.toString());
        // proceed(): 다음 메소드로 진행된다.
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
