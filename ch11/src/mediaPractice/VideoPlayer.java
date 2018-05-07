package mediaPractice;

import javax.media.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class VideoPlayer extends JFrame implements ControllerListener {

    public VideoPlayer() {
        super("Video Player");
        init();
    }

    private void init() {
        JMenu mnFile = new JMenu("File");
        JMenuItem miOpen = new JMenuItem("Open");
        ActionListener actionListener = new BtnActionListener();
        miOpen.addActionListener(actionListener);
        mnFile.add(miOpen);
        mnFile.addSeparator();
        JCheckBoxMenuItem miLoop = new JCheckBoxMenuItem("Loop");
        miLoop.addItemListener(e -> loop = !loop);
        mnFile.add(miLoop);
        mnFile.addSeparator();
        JMenuItem miExit = new JMenuItem("Exit");
        miExit.addActionListener(actionListener);
        mnFile.add(miExit);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(mnFile);
        setJMenuBar(menuBar);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setVisible(true);
    }


    private class BtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Open")) {
                openFile();
            } else {
                if (player != null) {
                    player.close();
                }
                System.exit(0);
            }
        }
    }

    private void openFile() {
        FileDialog fileDialog = new FileDialog(this, "Choose file", FileDialog.LOAD);
        fileDialog.setDirectory(path);
        fileDialog.setVisible(true);
        String file = fileDialog.getFile();
        if (file != null) {
            path = fileDialog.getDirectory();
            System.out.println(path);
            if (player != null) {
                player.close();
            }
            try {
                player = Manager.createPlayer(new MediaLocator(new URL("https://corpus.linguistics.berkeley.edu/acip/course/chapter1/clip_1.mp4")));
            } catch (IOException | NoPlayerException e) {
                e.printStackTrace();
                return;
            }
            if (player == null) {
                return;
            }
            this.setTitle(file);
            player.addControllerListener(this);
            player.prefetch();
        }

    }


    @Override
    public void controllerUpdate(ControllerEvent e) {
        if (e instanceof ControllerClosedEvent){
            if (video != null) {
                getContentPane().remove(video);
                video = null;
            }
            if (control != null) {
                getContentPane().remove(control);
                control = null;
            }
        } else if (e instanceof EndOfMediaEvent) {
            if (loop) {
                player.setMediaTime(new Time(0));
                player.start();
            }
        } else if (e instanceof PrefetchCompleteEvent) {
            player.start();
        } else if (e instanceof RealizeCompleteEvent) {
            video = player.getVisualComponent();
            control = player.getControlPanelComponent();
            if (video != null) {
                getContentPane().add(video);
            }
            if (control != null) {
                getContentPane().add(control, BorderLayout.SOUTH);
            }
            pack();
        }
    }

    private boolean loop;
    private String path;
    private Player player;
    private Component video;
    private Component control;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VideoPlayer());
    }
}
