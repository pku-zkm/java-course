package netPractice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection extends Thread {

    public Connection(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ignored) {
        }
    }

    public void send(String msg){
        out.println(msg);
        out.flush();
    }

    public String receive(){
        try {
            return in.readLine();
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    public void run() {
        while (true) {
            String msg = receive();
            if (msg != null) {
                server.receiveMsg(msg);
            }
        }
    }

    private PrintWriter out;
    private BufferedReader in;
    private ChatServer server;
    private Socket socket;
}
