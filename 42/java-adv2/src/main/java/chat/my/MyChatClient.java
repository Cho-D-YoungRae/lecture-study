package chat.my;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import static util.MyLogger.log;

public class MyChatClient {

    private static final int PORT = 12345;
    private static AtomicBoolean exit = new AtomicBoolean();

    public static void main(String[] args) throws IOException {
        log("클라이언트 시작");

        Scanner scanner = new Scanner(System.in);
        System.out.print("입력: ");
        String command = scanner.nextLine();

        if (!command.startsWith("/join|")) {
            System.out.println("입력이 잘못되었습니다. 종료합니다.");
            return;
        }

        Socket socket = new Socket("localhost", PORT);
        log("소캣 연결: " + socket);

        Thread readHandler = new Thread(() -> {
            try(socket;BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                while (!exit.get()) {
                    String received = br.readLine();
                    if (received == null) {
                        exit.set(true);
                        break;
                    }
                    System.out.println(received);
                }
            } catch (IOException e) {
                exit.set(true);
                log(e);
            }
        }, "reader-handler");

        Thread writeHandler = new Thread(() -> {
            try(socket; BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                while (!exit.get()) {
                    String request = scanner.nextLine();
                    System.out.println("request = " + request);
                    bw.write(request);
                }
            } catch (IOException e) {
                exit.set(true);
                log(e);
            }
        }, "writer-handler");

        readHandler.start();
        writeHandler.start();
    }
}
