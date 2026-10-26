package tobyspring.splearn;

import org.assertj.core.api.AssertProvider;
import org.springframework.test.json.JsonPathValueAssert;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertThatUtils {

    public static Consumer<AssertProvider<JsonPathValueAssert>> equalsTo(Object expected) {
        return value -> assertThat(value).isEqualTo(expected);
    }

    public static Consumer<AssertProvider<JsonPathValueAssert>> nonNull() {
        return value -> assertThat(value).isNotNull();
    }
}
