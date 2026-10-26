package com.example.defaultmethod.ex1;

public class SmsNotifier implements Notifier {

    @Override
    public void notify(String message) {
        System.out.println("[SMS] " + message);
    }

}
