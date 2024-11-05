package annotation.validator;

import java.lang.reflect.Field;

public class Validator {

    // Team, User
    public static void validate(Object obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(NotEmpty.class)) {
                Object value = field.get(obj);
                NotEmpty annotation = field.getAnnotation(NotEmpty.class);
                if (value == null || value.toString().isEmpty()) {
                    throw new RuntimeException(annotation.message());
                }
            }

            if (field.isAnnotationPresent(Range.class)) {
                long value = field.getLong(obj);
                Range annotation = field.getAnnotation(Range.class);

                if (value < annotation.min() || value > annotation.max()) {
                    throw new RuntimeException(annotation.message());
                }
            }
        }
    }
}
