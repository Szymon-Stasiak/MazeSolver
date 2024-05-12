package Gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class LoadingWindow {    
    private LoadingWindow() {}

    static void show(String title, Runnable bgActions) {
        JFrame loadingWindow = new JFrame();
        loadingWindow.setUndecorated(true); // Make the window frameless
        loadingWindow.setSize(300, 100);
        loadingWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(title + "...");

        loadingWindow.add(progressBar);
        loadingWindow.setVisible(true);

        loadingWindow.setLocationRelativeTo(null);
        loadingWindow.setAlwaysOnTop(true); // Make the window always on top

        Thread bgThread = new Thread(() -> {
            bgActions.run();
            loadingWindow.dispose();
        });
        bgThread.start();
    }
}
