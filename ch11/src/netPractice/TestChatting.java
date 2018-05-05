package netPractice;

public class TestChatting {
    public static void main(String[] args) {
        new Thread(() -> {
            new ChatServer();
        }).start();
        new Thread(() -> {
            new ChatClient();
        }).start();
    }
}
