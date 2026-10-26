package com.example.optional.logger;

public class LogMain2 {

    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.setDebugged(true);
        logger.debug(value100() + value200());

        logger.setDebugged(false);
        logger.debug(value100() + value200());

        if (logger.isDebugged()) {
            logger.debug(value100() + value200());
        }
    }

    static int value100() {
        System.out.println("LogMain2.value100");
        return 100;
    }

    static int value200() {
        System.out.println("LogMain2.value200");
        return 200;
    }
}
