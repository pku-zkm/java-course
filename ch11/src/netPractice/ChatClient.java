package netPractice;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends JFrame implements Runnable {

    public ChatClient() {
        super("Chat Client");
        init();
    }

    private void init() {
        list = new List();
        getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
        tfMsg = new JTextField();
        tfIp = new JTextField(15);
        tfPort = new JTextField(6);
        btnStart = new JButton("Connect");
        btnStart.addActionListener(e -> connect());
        btnSend = new JButton("Send");
        btnSend.addActionListener(e -> send());
        JPanel pnlSend = new JPanel(new BorderLayout());
        pnlSend.add(tfMsg);
        pnlSend.add(btnSend, BorderLayout.EAST);
        JPanel pnlIpPort = new JPanel(new FlowLayout());
        pnlIpPort.add(new JLabel("IP: "));
        pnlIpPort.add(tfIp);
        pnlIpPort.add(new JLabel("Port: "));
        pnlIpPort.add(tfPort);
        pnlIpPort.add(btnStart);
        JPanel pnl = new JPanel(new GridLayout(2, 1));
        pnl.add(pnlSend);
        pnl.add(pnlIpPort);
        getContentPane().add(pnl, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setVisible(true);
    }

    private void connect() {
        int port = Integer.parseInt(tfPort.getText());
        String ip = tfIp.getText();
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            list.add("Connection succeeded!");
            bConnected = true;
            new Thread(this).start();
        } catch (IOException e) {
            list.add("Connection failed!");
        }
    }

    private void send() {
        if (!bConnected) {
            list.add("Not connected!");
            return;
        }
        String msg = tfMsg.getText();
        tfMsg.setText("");
        if (msg.length() > 0) {
            out.println(msg);
            out.flush();
        }
    }

    private void receive() {
        try {
            String msg = in.readLine();
            list.add(msg);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void run() {
        while (true) {
            receive();
        }
    }

    public static void main(String[] args) {
        new ChatClient();
    }

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private JTextField tfMsg;
    private JTextField tfIp;
    private JTextField tfPort;
    private JButton btnStart;
    private JButton btnSend;
    private List list;
    private boolean bConnected;

}
