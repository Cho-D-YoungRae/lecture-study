package org.example;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Iterator;

// 구독 정보(구독자, 어떤 데이터를 구독할지)
public class MySubscription implements Subscription {

    private final Subscriber<? super Integer> subscriber;

    private final Iterator<Integer> it;

    public MySubscription(Subscriber<? super Integer> subscriber, Iterable<Integer> it) {
        this.subscriber = subscriber;
        this.it = it.iterator();
    }

    @Override
    public void request(long n) {   // n: 몇 개씩 줄지
        while (n > 0 && it.hasNext()) {
            subscriber.onNext(it.next());
            n--;
        }
        subscriber.onComplete();
    }

    @Override
    public void cancel() {

    }
}
