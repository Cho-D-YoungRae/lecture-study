package annotation.java;

public class DeprecatedMain {

    public static void main(String[] args) {
        System.out.println("DeprecatedMain.main");
        DeprecatedClass dc = new DeprecatedClass();
        dc.call1();
        dc.call2();
        dc.call3();
    }
}
