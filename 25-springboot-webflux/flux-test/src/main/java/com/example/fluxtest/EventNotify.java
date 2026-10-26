package com.example.fluxtest;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
@Getter
public class EventNotify {

    private final Queue<String> events = new LinkedList<>();

    public void add(String data) {
        events.offer(data);
    }
}
