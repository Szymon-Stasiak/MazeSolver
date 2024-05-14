package Gui;

import Maze.Maze;
import Maze.MazeSolver;
import Maze.Node;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainView {
    private static MainView instance;

    private Maze currentMaze = null;
    private MazePanel centerPanel;
    private JPanel leftPanel;
    
    boolean solved = false;

    public synchronized static MainView getInstance() {
        if (instance == null) {
            instance = new MainView();
        }
        return instance;
    }

    private MainView() {
        centerPanel = createCenterView();
        leftPanel = createLeftPanel();
    }
    
    private void AddMainButtons(JPanel panel) {
        JLabel mazeLabel = new JLabel("Current maze:");
        mazeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mazeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        mazeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField fileTextField = new JTextField();
        fileTextField.setEditable(false);
        fileTextField.setFocusable(false);
        fileTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        fileTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileTextField.setAlignmentY(Component.CENTER_ALIGNMENT);
        fileTextField.setHorizontalAlignment(JTextField.CENTER);
        fileTextField.setFont(new Font("Arial", Font.PLAIN, 15));

        JButton uploadButton = new JButton("Upload Maze");
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Maze file", "txt");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/src/main/resources"));
                if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    LoadingWindow.show("Loading Maze", () -> {
                        currentMaze = new Maze(fileChooser.getSelectedFile().getAbsolutePath());
                        fileTextField.setText(fileChooser.getSelectedFile().getName());
                        
                        updateImage();
                        centerPanel.setStartNode(new Point(currentMaze.getStart().getY(), currentMaze.getStart().getX()));
                        centerPanel.setEndNode(new Point(currentMaze.getEnd().getY(), currentMaze.getEnd().getX()));
                    });
                }
            }
        });
        
        uploadButton.setFocusable(false);
        uploadButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        

        JButton solveButton = new JButton("Solve Maze");
        solveButton.setPreferredSize(new Dimension(150, 25));
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solveMaze();
            }
        });

        solveButton.setFocusable(false);
        solveButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        solveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(mazeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fileTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(uploadButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)) );
        panel.add(solveButton);
    }

    private void AddSettingsButtons(JPanel panel) {
        JCheckBox autoSolve = new JCheckBox("Auto Solve");
        autoSolve.setFocusable(false);
        autoSolve.setAlignmentX(Component.CENTER_ALIGNMENT);
        autoSolve.setAlignmentY(Component.CENTER_ALIGNMENT);
        autoSolve.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                centerPanel.setAutoSolve(autoSolve.isSelected());
            }
        });
        panel.add(autoSolve);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.add(Box.createVerticalGlue());
        AddMainButtons(panel);
        panel.add(Box.createVerticalGlue());
        //AddSettingsButtons(panel);
        
        panel.setBackground(new Color(0xE5E1DA));
        return panel;
    }

    private MazePanel createCenterView() {
        MazePanel panel = new MazePanel(null);
        panel.setBackground(new Color(0xFBF9F1));
        panel.addMoveDisplay();     
        panel.addScaleDisplay();
        panel.addNavigationButtons();
        panel.addInteractiveImage();
        return panel;
        }

    public void updateImage() {
        if(currentMaze == null) return;
        BufferedImage image = GuiUtilities.getInstance().mazeToImage(currentMaze);
        centerPanel.changeImage(image);
    }

    public void solveMaze() {
        if(currentMaze == null) return;

        LoadingWindow.show("Solving Maze", () -> {
            if(solved) currentMaze.clearMaze();
            Node start = currentMaze.getNode(centerPanel.getStartNode().y, centerPanel.getStartNode().x);
            Node end = currentMaze.getNode(centerPanel.getEndNode().y, centerPanel.getEndNode().x);
            MazeSolver.solveMaze(start, end, currentMaze);
            solved = true;
            updateImage();
        });
    }

    public void show() {
        JFrame frame = new JFrame("MazeSolver");
        frame.setSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    boolean isSolved() {
        return solved;
    }
}
