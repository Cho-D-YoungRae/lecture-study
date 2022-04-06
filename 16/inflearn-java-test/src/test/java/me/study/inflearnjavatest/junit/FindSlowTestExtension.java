package me.study.inflearnjavatest.junit;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.Objects;

public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    public static final String START_TIME_STORE_KEY = "START_TIME";

    private static final long THRESHOLD = 1000L;

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        store.put(START_TIME_STORE_KEY, System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        Long startTime = store.remove(START_TIME_STORE_KEY, long.class);
        long duration = System.currentTimeMillis() - startTime;

        Method requiredTestMethod = context.getRequiredTestMethod();
        if (duration > THRESHOLD && Objects.isNull(requiredTestMethod.getAnnotation(SlowTest.class))) {
            String testMethodName = requiredTestMethod.getName();
            System.out.printf("Please consider mark method [%s] with @SlowTest\n", testMethodName);
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();
        return context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
    }
}
