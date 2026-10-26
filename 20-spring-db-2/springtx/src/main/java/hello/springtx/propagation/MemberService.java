package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final LogRepository logRepository;

    // 트랜잭션 적용 X -> Repository 에만 적용되어 있음
    // - 트랜잭션이 각각 걸리는 경우 (각 save 마다)
    public void joinV1(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("=== memberRepository 호출 시작 ===");
        memberRepository.save(member);
        log.info("=== memberRepository 호출 종료 ===");

        log.info("=== logRepository 호출 시작 ===");
        logRepository.save(logMessage);
        log.info("=== logRepository 호출 종료 ===");
    }

    @Transactional
    public void joinV1_tx(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("=== memberRepository 호출 시작 ===");
        memberRepository.save(member);
        log.info("=== memberRepository 호출 종료 ===");

        log.info("=== logRepository 호출 시작 ===");
        logRepository.save(logMessage);
        log.info("=== logRepository 호출 종료 ===");
    }

    @Transactional
    public void joinV1_tx_inner_nonTx(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("=== memberRepository 호출 시작 ===");
        memberRepository.save_nonTx(member);
        log.info("=== memberRepository 호출 종료 ===");

        log.info("=== logRepository 호출 시작 ===");
        logRepository.save_nonTx(logMessage);
        log.info("=== logRepository 호출 종료 ===");
    }

    @Transactional
    public void joinV2(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("=== memberRepository 호출 시작 ===");
        memberRepository.save(member);
        log.info("=== memberRepository 호출 종료 ===");

        log.info("=== logRepository 호출 시작 ===");
        try {
            logRepository.save(logMessage);
        } catch (RuntimeException e) {
            // 예외를 잡고 정상흐름으로 처리
            log.info("log 저장에 실패했습니다. logMessage={}", logMessage.getMessage());
            log.info("정상 흐름 반환");
        }
        log.info("=== logRepository 호출 종료 ===");
    }

    @Transactional
    public void joinV2_requiresNew(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("=== memberRepository 호출 시작 ===");
        memberRepository.save(member);
        log.info("=== memberRepository 호출 종료 ===");

        log.info("=== logRepository 호출 시작 ===");
        try {
            logRepository.save_requiresNew(logMessage);
        } catch (RuntimeException e) {
            // 예외를 잡고 정상흐름으로 처리
            log.info("log 저장에 실패했습니다. logMessage={}", logMessage.getMessage());
            log.info("정상 흐름 반환");
        }
        log.info("=== logRepository 호출 종료 ===");
    }
}
