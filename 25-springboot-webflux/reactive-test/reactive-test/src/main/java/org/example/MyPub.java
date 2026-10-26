package org.example;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.Arrays;

public class MyPub implements Publisher<Integer> {

    Iterable<Integer> it = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    @Override
    public void subscribe(Subscriber<? super Integer> s) {
        System.out.println("구독자: 신문사야 나 너희 신문 볼게");
        System.out.println("신문사: 구독 정보를 만들어서 줄테니 기다려!!");
        MySubscription subscription = new MySubscription(s, it);
        System.out.println("신문사: 구독 정보 생성 완료했어");
        s.onSubscribe(subscription);
    }
}
