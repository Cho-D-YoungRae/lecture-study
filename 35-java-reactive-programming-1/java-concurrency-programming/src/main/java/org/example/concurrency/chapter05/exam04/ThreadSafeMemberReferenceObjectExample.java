package org.example.concurrency.chapter05.exam04;

public class ThreadSafeMemberReferenceObjectExample {

    public static void main(String[] args) throws InterruptedException {
        // 스레드에 안전함, 멤버 변수를 공유하지 않음
        new Thread(new MyRunnable(new Company("User"))).start();
        new Thread(new MyRunnable(new Company("User"))).start();

        Thread.sleep(1000);
        System.out.println("=".repeat(50));

        Company company = new Company("User");
        new Thread(new MyRunnable(company)).start();
        new Thread(new MyRunnable(company)).start();
    }

    static class MyRunnable implements Runnable {

        private Company company;

        public MyRunnable(Company company) {
            this.company = company;
        }

        @Override
        public void run() {
            company.changeName(Thread.currentThread().getName());
        }
    }

    static class Company {
        private Member member;

        public Company(String name) {
            this.member = new Member();
            member.setName(name);
        }

        public synchronized void changeName(String name) {
            String oldName = member.getName();
            member.setName(name);
            System.out.println(Thread.currentThread().getName() + " : " + oldName + " -> " + name);
        }
    }

    static class Member {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
