package hello.hellospring;

import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

// 자바 코드로 직접 스프링 빈 등록하기
@Configuration
public class SpringConfig {

//    private DataSource dataSource;
//
//    // Configuration 도 스프링 빈으로 관리되기 때문에
//    // 스프링이 설정파일을 보고 자체적으로 빈을 생성해준다. -> DataSource 만들어서 주입.
//    @Autowired
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

//    private EntityManager em;
//    @Autowired
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }
    // 스프링 데이터 JPA 가 JpaRepository 를 상속하는 인터페이스의
    // 구현체를 만들어서 스프링 빈에 등록을 해준다 -> 가져다 쓰기만 하면 된다.
    private final MemberRepository memberRepository;
    // 그냥 injection 받으면 스프링 데이터 JPA 가 만들어 놓은 구현체가 등록된다.
    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    // 스프링 데이터 JPA를 사용해 생성자에서 MemberRepository 주입되므로 아래 필요 없음
//    @Bean
//    public MemberRepository memberRepository() {
////        return new MemoryMemberRepository();
////        return new JdbcMemberRepository(dataSource);
////        return new JdbcTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(em);
//    }
}
