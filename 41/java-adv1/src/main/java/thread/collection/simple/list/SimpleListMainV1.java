package thread.collection.simple.list;

public class SimpleListMainV1 {

    public static void main(String[] args) {
        SimpleList list = new BasicList();

        // 스레드1, 스레드2 가 동시에 실행 가정
        list.add("A"); // 스레드1 실행 가정
        list.add("B"); // 스레드2 실행 가정
        System.out.println(list);
    }
}
