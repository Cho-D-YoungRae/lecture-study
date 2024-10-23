package thread.control;

public class CheckedExceptionMain {

    public static void main(String[] args) {

    }

    static class CheckedRunnable implements Runnable {

        // 아래 주석 풀면 예외 발생
        @Override
        public void run() /*throws Exception*/ {
            // throw new Exception();
        }
    }
}
