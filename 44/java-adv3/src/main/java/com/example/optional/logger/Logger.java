package com.example.optional.logger;

import java.util.function.Supplier;

public class Logger {

    private boolean debugged = false;

    public void debug(Object mes) {
        if (debugged) {
            System.out.println("[DEBUG] " + mes);
        }
    }

    public boolean isDebugged() {
        return debugged;
    }

    public void setDebugged(boolean debugged) {
        this.debugged = debugged;
    }

    public void debug(Supplier<?> supplier) {
        if (debugged) {
            System.out.println("[DEBUG] " + supplier.get());
        }
    }
}
