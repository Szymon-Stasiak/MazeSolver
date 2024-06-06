package Gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public class SettingsView extends JDialog {
    private static MainView parentView;
    private static SettingsView settingsView = null;

    private static boolean disableWarning = false;

    ColorList<String> colorList;
    boolean mazeNeedRedraw = false;

    private JPanel colorSettings() {
        JPanel panel = new JPanel();
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        panel.setLayout(new BorderLayout());
        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setPreviewPanel(new JPanel());
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[0]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);

        

        ArrayList<ColorPack<String>> colorPacks = new ArrayList<>();

        colorPacks.add(new ColorPack<String>("Path", parentView.getMazePanel().getPathColor()));
        colorPacks.add(new ColorPack<String>("Start", parentView.getMazePanel().getStartColor()));
        colorPacks.add(new ColorPack<String>("End", parentView.getMazePanel().getEndColor()));
        colorPacks.add(new ColorPack<String>("Select", parentView.getMazePanel().getSelectColor()));
        colorPacks.add(new ColorPack<String>("Empty", parentView.getMazePanel().getEmptyColor()));
        colorPacks.add(new ColorPack<String>("Wall", parentView.getMazePanel().getWallColor()));

        colorList = new ColorList<>(colorPacks.toArray(new ColorPack[0]), 20, 2);
        colorList.setAlignmentX(CENTER_ALIGNMENT);
        colorList.setAlignmentY(CENTER_ALIGNMENT);

        JLabel colorLabel = new JLabel("Select color: ");
        colorLabel.setAlignmentX(CENTER_ALIGNMENT);
        colorLabel.setAlignmentY(CENTER_ALIGNMENT);

        listPanel.add(Box.createHorizontalStrut(100));
        listPanel.add(Box.createVerticalGlue());
        listPanel.add(colorLabel);
        listPanel.add(Box.createVerticalStrut(10));
        listPanel.add(colorList);
        listPanel.add(Box.createVerticalStrut(50));
        listPanel.add(Box.createVerticalGlue());

        colorList.setSelectedIndex(0);

        colorList.addListSelectionListener(e -> {
            ColorPack<String> selected = colorList.getSelectedValue();
            colorChooser.setColor(selected.getColor());

            if(selected.getValue() == "Empty" || selected.getValue() == "Wall" || (selected.getValue() == "Path" && parentView.getMazePanel().isSolved())) {
               mazeNeedRedraw = true; 
            }
        });

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Color changed");
                
                ColorPack<String> selected = colorList.getSelectedValue();
                selected.setColor(colorChooser.getColor().getRGB());
                colorList.repaint();
            }
        });

        panel.add(listPanel, BorderLayout.WEST);
        panel.add(colorChooser, BorderLayout.CENTER);

        return panel;
    }

    private JPanel advancedSettings() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel saveRescaleLabel = new JLabel("Save upscaling: ");
        saveRescaleLabel.setAlignmentX(CENTER_ALIGNMENT);
        saveRescaleLabel.setAlignmentY(CENTER_ALIGNMENT);

        JSlider saveRescaleSlider = new JSlider(1, 10, GuiUtilities.getInstance().getSaveScale());
        saveRescaleSlider.setMajorTickSpacing(1);
        saveRescaleSlider.setPaintTicks(true);
        saveRescaleSlider.setPaintLabels(true);
        saveRescaleSlider.setSnapToTicks(true);
        saveRescaleSlider.setMaximumSize(new Dimension(400, 50));
        saveRescaleSlider.setAlignmentX(CENTER_ALIGNMENT);
        saveRescaleSlider.setAlignmentY(CENTER_ALIGNMENT);


        saveRescaleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                GuiUtilities.getInstance().setSaveScale(saveRescaleSlider.getValue());
            }
        });

        JLabel disableWarningsLabel = new JLabel("Disable warnings: ");
        disableWarningsLabel.setAlignmentX(CENTER_ALIGNMENT);
        disableWarningsLabel.setAlignmentY(CENTER_ALIGNMENT);
        
        JCheckBox disableWarningsCheck = new JCheckBox();
        disableWarningsCheck.setSelected(disableWarning);
        disableWarningsCheck.addChangeListener(e -> {
            disableWarning = disableWarningsCheck.isSelected();
        });
        disableWarningsCheck.setAlignmentX(CENTER_ALIGNMENT);
        disableWarningsCheck.setAlignmentY(CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalGlue());
        panel.add(saveRescaleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(saveRescaleSlider);
        panel.add(Box.createVerticalStrut(20));
        panel.add(disableWarningsLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(disableWarningsCheck);
        panel.add(Box.createVerticalGlue());


        return panel;
    }

    private void AddTabs() {
        JTabbedPane tabManager = new JTabbedPane();

        
        tabManager.addTab("Colors", colorSettings());
        tabManager.addTab("Advanced", advancedSettings());
        add(tabManager);
    }

    public static SettingsView get(MainView parent) {
        parentView = parent;
        if(settingsView == null) {
            settingsView = new SettingsView();
        }
        return settingsView;
    }

    private SettingsView() {
        super();
        setModal(true);
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        AddTabs();
        setMinimumSize(new Dimension(800, 400));

    }

    private void updateColors() {
        parentView.getMazePanel().setPathColor(colorList.getModel().getElementAt(0).getColor());
        parentView.getMazePanel().setStartColor(colorList.getModel().getElementAt(1).getColor());
        parentView.getMazePanel().setEndColor(colorList.getModel().getElementAt(2).getColor());
        parentView.getMazePanel().setSelectColor(colorList.getModel().getElementAt(3).getColor());
        parentView.getMazePanel().setEmptyColor(colorList.getModel().getElementAt(4).getColor());
        parentView.getMazePanel().setWallColor(colorList.getModel().getElementAt(5).getColor());
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if(b == false) {
            updateColors();
            if(mazeNeedRedraw) parentView.getMazePanel().redrawMaze();
            mazeNeedRedraw = false;
        }
    }

    public static boolean getDisableWarning() {
        return disableWarning;
    }
}
