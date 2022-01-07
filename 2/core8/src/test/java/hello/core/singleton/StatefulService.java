package hello.core.singleton;

// 상태를 저장하지 않도록 변경
public class StatefulService {

//    private int price;

    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price;
    }
}
