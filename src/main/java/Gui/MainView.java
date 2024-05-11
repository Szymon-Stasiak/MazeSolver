package Gui;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainView {
    private static MainView instance;

    private String currentImage = null;
    private ImagePanel centerPanel;
    private JPanel leftPanel;


    public synchronized static MainView getInstance() {
        if (instance == null) {
            instance = new MainView();
        }
        return instance;
    }

    private MainView() {
        currentImage = new String("");
        centerPanel = createCenterView();
        leftPanel = createLeftPanel();
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField fileTextField = new JTextField();
        //fileTextField.setAlignmentY(Component.CENTER_ALIGNMENT);
        fileTextField.setEditable(false);
        fileTextField.setFocusable(false);

        JButton uploadButton = new JButton("Upload Image");
        uploadButton.setPreferredSize(new Dimension(150, 25));
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);
                if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    currentImage = fileChooser.getSelectedFile().getAbsolutePath();
                    fileTextField.setText(fileChooser.getSelectedFile().getName());

                    updateImage();
                }
            }
        });
        uploadButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        fileTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        panel.add(Box.createVerticalGlue());
        panel.add(fileTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(uploadButton);
        panel.add(Box.createVerticalGlue());

        panel.setBackground(new Color(205, 232, 229));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(122, 178, 178)));

        return panel;
    }

    private ImagePanel createCenterView() {
        ImagePanel panel = new ImagePanel(null);
        panel.addMoveDisplay();     
        panel.addScaleDisplay();
        return panel;
    }

    public void updateImage() {
        if(currentImage == null) return;
        Graphics graphics = centerPanel.getGraphics();
        BufferedImage image = null;

        try {
        image = ImageIO.read(new File(currentImage));
        } catch (IOException e) {
            e.printStackTrace();    
        }
        if(image == null) return;
        
        centerPanel.changeImage(image);
    }

    public void show() {
        JFrame frame = new JFrame("MainView");
        frame.setSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
