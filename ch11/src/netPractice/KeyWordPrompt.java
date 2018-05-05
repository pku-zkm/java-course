package netPractice;

import org.apache.http.client.fluent.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class KeyWordPrompt extends JFrame {

    public KeyWordPrompt() {
        super("Prompt");
        tf = new JTextField();
        lst = new JList<>();
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                getKeyWord();
            }
        });
        lst.addListSelectionListener(e -> tf.setText(lst.getSelectedValue()));
        getContentPane().add(tf, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(lst), BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }


    private void getKeyWord() {
        String text = tf.getText();
        if (text == null || text.length() == 0) {
            SwingUtilities.invokeLater(() -> {
                lst.setListData(new String[0]);
                lst.updateUI();
            });
        } else {
            new Thread(() -> {
                try {
                    String content = Request.Get("http://suggestion.baidu.com/su?wd=" + text).execute().returnContent().toString();
                    if (content == null || content.length() == 0) {
                        SwingUtilities.invokeLater(() -> {
                            lst.removeAll();
                            lst.updateUI();
                        });
                        return;
                    }
                    String[] words = content.replaceAll(".*s:\\[([^]]*)].*", "$1").replaceAll("\"", "").split(",");
                    SwingUtilities.invokeLater(() -> {
                        lst.setListData(words);
                        lst.updateUI();
                    });
                } catch (IOException | IllegalArgumentException ignored) {
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(KeyWordPrompt::new);
    }

    private JTextField tf;
    private JList<String> lst;
}
