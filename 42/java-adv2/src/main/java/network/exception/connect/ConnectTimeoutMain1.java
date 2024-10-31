package network.exception.connect;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import static util.MyLogger.log;

public class ConnectTimeoutMain1 {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        try {
            Socket socket = new Socket("192.168.1.250", 45678);
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        log("end = " + (end - start));
    }
}
