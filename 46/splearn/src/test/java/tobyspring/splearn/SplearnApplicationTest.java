package tobyspring.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class SplearnApplicationTest {

    @Test
    void run() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            SplearnApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[]{}));
        }
    }
}