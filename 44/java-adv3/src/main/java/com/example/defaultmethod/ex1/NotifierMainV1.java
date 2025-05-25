package com.example.defaultmethod.ex1;

import java.util.List;

public class NotifierMainV1 {

    public static void main(String[] args) {
        List<Notifier> notifiers = List.of(
                new EmailNotifier(),
                new SmsNotifier(),
                new AppPushNotifier()
        );
        notifiers.forEach(n -> n.notify("서비스 가입을 환영합니다!"));


    }
}
