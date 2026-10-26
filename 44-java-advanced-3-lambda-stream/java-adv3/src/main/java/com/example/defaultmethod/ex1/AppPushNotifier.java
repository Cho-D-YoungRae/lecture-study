package com.example.defaultmethod.ex1;

public class AppPushNotifier implements Notifier {

    @Override
    public void notify(String message) {
        System.out.println("[APP PUSH] " + message);
    }

}
