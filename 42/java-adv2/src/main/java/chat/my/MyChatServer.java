package chat.my;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

import static util.MyLogger.log;

public class MyChatServer {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        log("서버 시작");
        MyChatSessionManager sessionManager = new MyChatSessionManager();
        ServerSocket serverSocket = new ServerSocket(PORT);
        log("서버 소캣 시작 - 리스닝 포트: " + PORT);

        // ShutdownHook 등록
        ShutdownHook shutdownHook = new ShutdownHook(serverSocket, sessionManager);
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook, "shutdown"));

        Pattern joinCommandPattern = Pattern.compile("/join\\|[A-Za-z0-9]+");

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                log("소캣 연결: " + socket);

                try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String command = br.readLine();
                    if (!joinCommandPattern.matcher(command).matches()) {
                        throw new IllegalArgumentException("[" + socket + "] 올바르지 않은 명령어: " + command);
                    }

                    String name = command.substring(6);
                    MyChatSession session = new MyChatSession(socket, sessionManager, name);
                    session.start();

                } catch (IllegalArgumentException e) {
                    log(e);
                    socket.close();
                    log("소캣 종료: " + socket);
                }

            }
        } catch (IOException e) {
            log("서버 소캣 종료: " + e);
        }

    }

    static class ShutdownHook implements Runnable {

        private final ServerSocket serverSocket;
        private final MyChatSessionManager sessionManager;

        public ShutdownHook(ServerSocket serverSocket, MyChatSessionManager sessionManager) {
            this.serverSocket = serverSocket;
            this.sessionManager = sessionManager;
        }

        @Override
        public void run() {
            log("shutdownHook 실행");
            try {
                sessionManager.closeAll();
                serverSocket.close();

                Thread.sleep(1000); // 자원 정리 대기
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("e = " + e);
            }
        }
    }

}