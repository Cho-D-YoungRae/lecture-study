package com.example.optional.logger;

public class LogMain1 {

    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.setDebugged(true);
        logger.debug(10 + 20);

        logger.setDebugged(false);
        logger.debug(100 + 200);
    }
}
