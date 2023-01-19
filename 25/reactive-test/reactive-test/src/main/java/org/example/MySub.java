package org.example;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MySub implements Subscriber<Integer> {

    private Subscription s;

    private int bufferSize = 2;

    @Override
    public void onSubscribe(Subscription s) {
        System.out.println("구독자: 구독정보 잘 받았어");
        this.s = s;
        System.out.println("구독자: 나 이제 신문 1개씩 줘");
        s.request(bufferSize);
    }

    @Override
    public void onNext(Integer item) {
        System.out.println("onNext() => " + item);

        /**
         * 그 다음에 또 다시 전달
         * 아래와 같이 하면 2번에 한번씩 하루 지남이 호출되는 것이 아니라
         * 하루지남은 계속 1번씩 호출됨
         */
//        System.out.println("하루 지남");
//        s.request(2);

        bufferSize--;
        if (bufferSize == 0) {
            bufferSize = 2;
            System.out.println("하루 지남");
            s.request(bufferSize);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("구독 중 에러");
    }

    @Override
    public void onComplete() {
        System.out.println("구독 완료");
    }
}
