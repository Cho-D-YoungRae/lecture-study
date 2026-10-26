package hello.selector;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

class ImportSelectorTest {

    @Test
    void staticConfig() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                StaticConfig.class);
        HelloBean bean = applicationContext.getBean(HelloBean.class);
        assertThat(bean).isNotNull();
    }

    @Test
    void selectorConfig() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                SelectorConfig.class);
        HelloBean bean = applicationContext.getBean(HelloBean.class);
        assertThat(bean).isNotNull();
    }

    @Configuration
    @Import(HelloConfig.class)
    public static class StaticConfig {
    }

    @Configuration
    @Import(HelloImportSelector.class)
    public static class SelectorConfig {

    }
}
