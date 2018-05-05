package netPractice;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer extends JFrame {

    public ChatServer(int port) {
        super("Chat Server");
        this.port = port;
        init();
        start();
    }

    public ChatServer() {
        this(9876);
    }

    private void init() {
        lst = new List();
        getContentPane().add(new JScrollPane(lst));
        JPanel pnl = new JPanel(new BorderLayout());
        tf = new JTextField();
        btn = new JButton("Send");
        btn.addActionListener(e -> sendMsg());
        pnl.add(tf, BorderLayout.CENTER);
        pnl.add(btn, BorderLayout.EAST);
        getContentPane().add(pnl, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setVisible(true);
    }

    private void sendMsg() {
        String msg = tf.getText();
        if (msg.length() > 0) {
            lst.add(msg);
            castMsg(msg);
            tf.setText("");
        }
    }

    private void start() {
        connections = new Vector<>();
        try {
            server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                Connection connection = new Connection(this, socket);
                connection.start();
                connections.add(connection);
                receiveMsg("A client came in.");
            }
        } catch (IOException ignored) {
        }
    }

    void receiveMsg(String msg) {
        lst.add(msg);
        castMsg(msg);
    }

    public void castMsg(String msg) {
        for (Connection connection : connections) {
            connection.send(msg);
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }

    private int port;
    private Vector<Connection> connections;
    private ServerSocket server;
    private List lst;
    private JTextField tf;
    private JButton btn;
}

