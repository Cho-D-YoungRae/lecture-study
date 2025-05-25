package com.example.defaultmethod.ex2;

import java.time.LocalDateTime;
import java.util.List;

public class NotifierMainV2 {

    public static void main(String[] args) {
        List<Notifier> notifiers = List.of(
                new EmailNotifier(),
                new SmsNotifier(),
                new AppPushNotifier()
        );
        notifiers.forEach(n -> n.notify("서비스 가입을 환영합니다!"));

        LocalDateTime play1Days = LocalDateTime.now().plusDays(1);
        notifiers.forEach(n -> n.scheduleNotification("Hello", play1Days));
    }
}
