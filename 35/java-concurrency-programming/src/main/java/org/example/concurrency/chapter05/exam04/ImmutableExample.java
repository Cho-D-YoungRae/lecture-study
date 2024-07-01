package org.example.concurrency.chapter05.exam04;

public class ImmutableExample implements Runnable {

    private ImmutablePerson person;

    public ImmutableExample(final ImmutablePerson person) {
        this.person = person;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " : " + person);
    }

    public static void main(String[] args) {
        ImmutablePerson person = new ImmutablePerson("홍길동", 25);

        for (int i = 0; i < 10; i++) {
            new Thread(new ImmutableExample(person)).start();
        }
    }

}

record ImmutablePerson(
    String name,
    int age
) {
}
