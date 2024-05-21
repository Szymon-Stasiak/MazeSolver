package Gui;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public abstract class LoadingWindow extends SwingWorker<Void, Void> {
    private String title;
    private JFrame loadingWindow;
    private JProgressBar progressBar;
    private boolean interrupted = false;
    
    LoadingWindow(String title) {
        super();
        this.title = title;
    }

    @Override
    protected Void doInBackground() throws Exception {
        if(!onStart()) {
            interrupted = true;
            return null;
        }
        loadingWindow = new JFrame();
        loadingWindow.setUndecorated(true); // Make the window frameless
        loadingWindow.setSize(300, 100);
        loadingWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(title + "...");

        loadingWindow.add(progressBar);
        loadingWindow.setVisible(true);

        loadingWindow.setLocationRelativeTo(null);
        loadingWindow.setAlwaysOnTop(true);

        bgWork();
        return null;
    }

    @Override
    protected void done() {
        if(interrupted) return;
        loadingWindow.dispose();
        onDone();
    }

    abstract protected void bgWork();
    protected void onDone() {}
    protected boolean onStart() {
        return true;
    }

    
    void show() {
        execute();
    }
}
