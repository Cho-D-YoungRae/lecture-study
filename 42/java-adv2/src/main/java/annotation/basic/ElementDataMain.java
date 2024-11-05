package annotation.basic;

import java.util.Arrays;

public class ElementDataMain {

    public static void main(String[] args) {
        Class<ElementData1> annoClass = ElementData1.class;
        AnnoElement annotation = annoClass.getAnnotation(AnnoElement.class);

        System.out.println("annotation.value() = " + annotation.value());
        System.out.println("annotation.count() = " + annotation.count());
        System.out.println("annotation.tags() = " + Arrays.toString(annotation.tags()));
    }
}
