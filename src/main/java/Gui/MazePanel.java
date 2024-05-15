package Gui;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MazePanel extends JPanel {
    //SETTINGS
    private boolean autoSolve = false;

    private float dragForce = 1.0f;
    private float scaleForce = 0.1f;
    private int arrowMoveForce = 25;

    //PRIVATE VAR
    private float scale = 1.0f;

    //HARDCODED SETTINGS
    private Dimension minSize = new Dimension(10, 10);
    private Dimension maxSize = new Dimension(100000, 100000);

    //DIMENSION POSITIONS
    private Point pos = new Point(0, 0);
    private Point imgPos = new Point(0, 0);

    //MAZE POSITIONS
    private Point startNode = null;
    private Point endNode = null;

    //MOUSE POSITION
    private Point oldMousePos = null;

    //LABELS
    private JLabel posLabel = null;
    private JLabel scaleLabel = null;

    //IMAGE
    private BufferedImage img;

    public MazePanel() {
        super();
        img = null;
    }

    //ADD MOVE TO IMAGE DISPLAY
    public void addMoveDisplay() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (img == null)
                    return;
                if (oldMousePos == null) {
                    oldMousePos = e.getPoint();
                    return;
                }

                imgPos.translate((int) ((e.getX() - oldMousePos.x) * dragForce),
                        (int) ((e.getY() - oldMousePos.y) * dragForce));
                oldMousePos = e.getPoint();
                repaint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                oldMousePos = null;
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        imgPos.translate(0, 1 * arrowMoveForce);
                        repaint();
                        break;
                    case KeyEvent.VK_DOWN:
                        imgPos.translate(0, -1 * arrowMoveForce);
                        repaint();
                        break;
                    case KeyEvent.VK_LEFT:
                        imgPos.translate(1 * arrowMoveForce, 0);
                        repaint();
                        break;
                    case KeyEvent.VK_RIGHT:
                        imgPos.translate(-1 * arrowMoveForce, 0);
                        repaint();
                        break;
                }
            }
        });
    }

    //SET NEW START
    public void setStartNode(Point startNode) {
        if (this.startNode != null) {
            if (!MainView.getInstance().isSolved()) {
                img.setRGB(this.startNode.x, this.startNode.y, Color.WHITE.getRGB());
            } else {
                img.setRGB(this.startNode.x, this.startNode.y, Color.RED.getRGB());
            }
        }

        this.startNode = startNode;

        if (autoSolve) {
            MainView.getInstance().solveMaze();
        }

        repaint();
    }

    //SET NEW END
    public void setEndNode(Point endNode) {
        if (this.endNode != null) {
            if (!MainView.getInstance().isSolved()) {
                img.setRGB(this.endNode.x, this.endNode.y, Color.WHITE.getRGB());
            } else {
                img.setRGB(this.endNode.x, this.endNode.y, Color.RED.getRGB());
            }
        }

        this.endNode = endNode;

        if (autoSolve) {
            MainView.getInstance().solveMaze();
        }

        repaint();
    }

    //SET AUTOSOLVE
    public void setAutoSolve(boolean autoSolve) {
        if (!MainView.getInstance().isSolved()) {
            MainView.getInstance().solveMaze();
        }

        this.autoSolve = autoSolve;
    }

    //ADD SCALING TO IMAGE DISPLAY
    public void addScaleDisplay() {
        this.addMouseWheelListener(e -> {
            if (img == null)
                return;
            float newScale = scale + scale * (float) e.getWheelRotation() * scaleForce;
            if (newScale * img.getWidth() < minSize.width || newScale * img.getHeight() < minSize.height
                    || newScale * img.getWidth() > maxSize.getWidth()
                    || newScale * img.getHeight() > maxSize.getHeight())
                return;
            scale = newScale;
            repaint();
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                float newScale = 0;
                switch (keyCode) {
                    case KeyEvent.VK_Z:
                        if (img == null)
                            return;
                        newScale = scale + scale * scaleForce;
                        if (newScale * img.getWidth() < minSize.width || newScale * img.getHeight() < minSize.height
                                || newScale * img.getWidth() > maxSize.getWidth()
                                || newScale * img.getHeight() > maxSize.getHeight())
                            return;
                        scale = newScale;
                        repaint();
                        break;
                    case KeyEvent.VK_X:
                        if (img == null)
                            return;
                        newScale = scale - scale * scaleForce;
                        if (newScale * img.getWidth() < minSize.width || newScale * img.getHeight() < minSize.height
                                || newScale * img.getWidth() > maxSize.getWidth()
                                || newScale * img.getHeight() > maxSize.getHeight())
                            return;
                        scale = newScale;
                        repaint();
                        break;
                }
            }
        });
    }

    //UPDATE INFO LABELS
    public void updateLabels() {
        if (posLabel == null || scaleLabel == null)
            return;
        posLabel.setText("X: " + imgPos.x + " Y: " + imgPos.y);
        scaleLabel.setText("  Scale: " + scale + "  ");
    }

    //ADD NAVIGATION BUTTONS + NAVIGATION LABELS
    public void addNavigationButtons() {
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        posLabel = new JLabel("X: " + pos.x + " Y: " + pos.y);
        scaleLabel = new JLabel("  Scale: " + scale + "  ");

        buttonPanel.add(posLabel);
        buttonPanel.add(scaleLabel);

        JButton rescaleButton = new JButton();
        ImageIcon searchIcon = new ImageIcon("C:\\Users\\golas\\JIMP\\MazeSolver\\src\\main\\graphics\\search.png");
        searchIcon.setImage(searchIcon.getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH));
        rescaleButton.setIcon(searchIcon);
        rescaleButton.setPreferredSize(new Dimension(40, 40));
        rescaleButton.setOpaque(false);
        rescaleButton.setContentAreaFilled(false);
        rescaleButton.setBorderPainted(false);
        rescaleButton.setFocusable(false);
        rescaleButton.addActionListener(e -> {
            rescale();
        });

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "rescaleButtonPressed");
        this.getActionMap().put("rescaleButtonPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rescaleButton.doClick();
            }
        });

        buttonPanel.add(rescaleButton);
        buttonPanel.setBackground(new Color(0xAAD7D9));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    //ADD INTERACTIONS TO IMAGE
    public void addInteractiveImage() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (img == null)
                    return;
                if (e.getButton() != MouseEvent.BUTTON3)
                    return;

                Point mousePos = e.getPoint();
                int x = (int) ((mousePos.x - imgPos.x) / scale);
                int y = (int) ((mousePos.y - imgPos.y) / scale);

                if (img.getRGB(x, y) != Color.white.getRGB()) {
                    return;
                }

                img.setRGB(x, y, Color.GREEN.getRGB());
                repaint();

                String[] options = { "Set start", "Set end" };
                Runnable[] actions = { () -> {
                    setStartNode(new Point(x, y));
                }, () -> {
                    setEndNode(new Point(x, y));
                } };

                OptionMenu menu = new OptionMenu("X: " + x + " Y: " + y, e.getPoint(), options, actions, () -> {
                    img.setRGB(x, y, Color.WHITE.getRGB());
                    repaint();
                });
                menu.setVisible(true);

                repaint();
            }
        });
    }

    public MazePanel(BufferedImage image) {
        super();
        img = image;
        if (autoSolve) {
            MainView.getInstance().solveMaze();
        }
    }

    public Point getStartNode() {
        return startNode;
    }

    public Point getEndNode() {
        return endNode;
    }

    //RESCALE TO FIT IMAGE
    public void rescale() {
        if (img == null)
            return;
        pos.setLocation(0, 0);

        float newScale = 1f;
        if (img.getWidth() > img.getHeight()) {
            newScale = 0.8f * (float) getWidth() / img.getWidth();
        } else {
            newScale = 0.8f * (float) getHeight() / img.getHeight();
        }

        imgPos.setLocation(getWidth() / 2, getHeight() / 2);
        imgPos.translate((int) ((float) img.getWidth() * newScale / -2f),
                (int) ((float) img.getHeight() * newScale / -2f));

        scale = newScale;
        repaint();
    }

    //CHANGE IMAGE TO NEW
    public void changeImage(BufferedImage image) {
        img = image;
        rescale();
    }

    //DRAW IN CURRENT STATE
    private void drawInState(Graphics g) {
        Point trueImgPos = new Point(imgPos.x + pos.x, imgPos.y + pos.y);
        img.setRGB(startNode.x, startNode.y, Color.magenta.getRGB());
        img.setRGB(endNode.x, endNode.y, Color.magenta.getRGB());
        g.drawImage(img, trueImgPos.x, trueImgPos.y, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale),
                null);
    }

    //ADD IMAGE PAINTER TO COMPONENT
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img == null)
            return;
        updateLabels();
        drawInState(g);
    }
}
