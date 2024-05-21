package Gui;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class SettingsView extends JDialog {
    private static MainView parentView;
    private static SettingsView settingsView = null;

    private JPanel colorSettings() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setPreviewPanel(new JPanel());
        
        panel.add(colorChooser, BorderLayout.CENTER);
        return panel;
    }

    private void AddTabs() {
        JTabbedPane tabManager = new JTabbedPane();
        
        tabManager.addTab("Colors", colorSettings());
        add(tabManager);
    }

    public static SettingsView get(MainView parent) {
        if(settingsView == null) {
            settingsView = new SettingsView();
        }
        parentView = parent;
        return settingsView;
    }

    private SettingsView() {
        super();
        setModal(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        AddTabs();
        setMinimumSize(new Dimension(800, 400));

    }
}
