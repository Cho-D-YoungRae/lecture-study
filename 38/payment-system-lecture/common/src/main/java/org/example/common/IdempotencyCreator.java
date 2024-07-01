package org.example.common;

import java.util.UUID;

public final class IdempotencyCreator {

    private IdempotencyCreator() {
        throw new UnsupportedOperationException();
    }

    public static String create(Object data) {
        return UUID.nameUUIDFromBytes(data.toString().getBytes()).toString();
    }
}
