package hello.core.scan.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.ComponentScan.*;

class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        ApplicationContext ac =
                new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);

        assertThat(beanA).isNotNull();

        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("beanB", BeanB.class));
    }

    // @Filter 의 type 은 FilterType.ANNOTATION 이 기본이므로 생략가능
    @Configuration
    @ComponentScan(includeFilters = @Filter(
            type = FilterType.ANNOTATION, classes = MyInculdeComponent.class))
    @ComponentScan(excludeFilters = @Filter(classes = MyExculdeComponent.class))
    static class ComponentFilterAppConfig {

    }
}
