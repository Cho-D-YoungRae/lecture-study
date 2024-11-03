package chat.my;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static util.MyLogger.log;

public class MyChatSession {

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final MyChatSessionManager sessionManager;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private String name;
    private boolean closed = false;

    public MyChatSession(Socket socket, MyChatSessionManager sessionManager, String name) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.sessionManager = sessionManager;
        this.sessionManager.add(this);
        this.name = name;
    }

    public void start() {

        Thread writeHandler = new Thread(() -> {
            while (!closed) {
                try {
                    String message = messageQueue.take();
                    writer.write(message);
                } catch (InterruptedException | IOException e) {
                    close();
                    throw new RuntimeException(e);
                }
            }
        }, "write-handler-" + socket.getInetAddress());

        Thread readHandler = new Thread(() -> {
            while (!closed) {
                try {
                    String received = reader.readLine();
                    log("client -> server: " + received);
                    if (received == null || "/exit".equals(received)) {
                        close();
                    } else if (received.startsWith("/message|")) {
                        String message = received.substring(9);
                        sessionManager.broadcast(name + ": " + message);
                    } else if (received.startsWith("/change|")) {
                        String newName = received.substring(8);
                        setName(newName);
                    } else if (received.startsWith("/users")) {
                        sendMessage("현재 접속자: " + sessionManager.getAllNames());
                    } else if (received.startsWith("/exit")) {
                        close();
                    } else {
                        sendMessage("잘못된 명령어입니다.");
                    }
                } catch (IOException e) {
                    close();
                    throw new RuntimeException(e);
                }
            }
        }, "read-handler-" + socket.getInetAddress());

        writeHandler.start();
        readHandler.start();
    }

    // 세션 종료시, 서버 종료시 동시에 호출될 수 있다.
    public synchronized void close() {
        if (closed) {
            return;
        }
        sessionManager.remove(this);
        close(reader);
        close(writer);
        close(socket);
        closed = true;
        log("연결 종료: " + socket);
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                log(e);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            close();
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
