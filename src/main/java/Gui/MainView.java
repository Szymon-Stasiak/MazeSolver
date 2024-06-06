package Gui;

import Maze.Maze;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MainView {
    private static MainView instance;

    private Dimension warningSize = new Dimension(300, 300);

    private MazePanel centerPanel;
    private JPanel leftPanel;


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
    
    private void addMainButtons(JPanel panel) {
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
                uploadMaze(fileTextField);
            }
        });
        
        uploadButton.setFocusable(false);
        uploadButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton solveButton = new JButton("Solve Maze");
        solveButton.setPreferredSize(new Dimension(150, 25));
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                centerPanel.solveMaze();
            }
        });

        solveButton.setFocusable(false);
        solveButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        solveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton saveButton = new JButton("Save Maze");
        saveButton.setPreferredSize(new Dimension(150, 25));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!centerPanel.imageLoaded()) return;

                LoadingWindow loading = new LoadingWindow("Scaling maze") {
                    private BufferedImage img;
                    @Override
                    protected boolean onStart() {
                        img = centerPanel.getImage();

                        if(img.getWidth() * img.getHeight() > warningSize.width * warningSize.height) {
                            if(SettingsView.getDisableWarning()) {
                                return true;
                            }
                            
                            int result = JOptionPane.showConfirmDialog(null, "The maze is large, do you wish to continue?", "Warning", JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.NO_OPTION) {
                                return false;
                            }
                        }
                        return true;
                    }

                    @Override
                    protected void bgWork() {
                        img = GuiUtilities.getInstance().scaleToSave(img);
                        System.out.println("Scaled image: " + img);
                    }

                    @Override
                    protected void onDone() {
                        JFileChooser fileChooser = new JFileChooser("C:/");
                        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));
                        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("gif", "gif"));
                        fileChooser.setAcceptAllFileFilterUsed(false);

                        if(fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
                            try {
                                String extension = fileChooser.getFileFilter().getDescription();
                                File outputfile = new File(fileChooser.getSelectedFile().getAbsolutePath() + "." + extension);
                                if (outputfile.exists()) {
                                    if(SettingsView.getDisableWarning()) {
                                        outputfile.delete();
                                    } else {
                                        int result = JOptionPane.showConfirmDialog(null, "The file already exists, do you want to overwrite it?", "File already exists", JOptionPane.YES_NO_OPTION);
                                        if (result == JOptionPane.YES_OPTION) {
                                            outputfile.delete();
                                        } else {
                                            return;
                                        }
                                    }            
                                }
                                ImageIO.write(img, extension, outputfile);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        }
                    }
                };

                loading.show();
            }
        });

        saveButton.setFocusable(false);
        saveButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(mazeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fileTextField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(uploadButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)) );
        panel.add(solveButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)) );
        panel.add(saveButton);
    }


    private void addSettingsButtons(JPanel panel) {
        JButton settingsButton = new JButton("Settings");
        ImageIcon settingsIcon = new ImageIcon(System.getProperty("user.dir") + "/src/main/graphics/settings.png");
        settingsIcon.setImage(settingsIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        settingsButton.setIcon(settingsIcon);
        settingsButton.setPreferredSize(new Dimension(110, 40));
        settingsButton.setOpaque(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusable(false);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SettingsView.get(MainView.instance).setVisible(true);
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(settingsButton);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JCheckBox autoSolve = new JCheckBox("Auto Solve");
        autoSolve.setFocusable(false);
        autoSolve.setAlignmentX(Component.CENTER_ALIGNMENT);
        autoSolve.setAlignmentY(Component.CENTER_ALIGNMENT);
        autoSolve.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                centerPanel.setAutoSolve(autoSolve.isSelected());
            }
        });

        addSettingsButtons(panel);
        panel.add(Box.createVerticalGlue());
        addMainButtons(panel);
        panel.add(Box.createVerticalGlue());
        panel.add(autoSolve);
        
        panel.setBackground(new Color(0xE5E1DA));
        return panel;
    }

    private MazePanel createCenterView() {
        MazePanel panel = new MazePanel();
        panel.setBackground(new Color(0xFBF9F1));
        panel.addMoveDisplay();     
        panel.addScaleDisplay();
        panel.addNavigationButtons();
        panel.addInteractiveMaze();
        return panel;
    }

    private void uploadMaze(JTextField fileTextField) {
        
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Maze file", "txt", "bin");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/src/main/resources"));
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            LoadingWindow loading = new LoadingWindow("Loading Maze") {
                @Override
                protected void bgWork() {
                    try {
                        centerPanel.changeMaze(new Maze(fileChooser.getSelectedFile().getAbsolutePath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    fileTextField.setText(fileChooser.getSelectedFile().getName());
                }
            };

            loading.show();
        }
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

    public MazePanel getMazePanel() {
        return centerPanel;
    }
}
