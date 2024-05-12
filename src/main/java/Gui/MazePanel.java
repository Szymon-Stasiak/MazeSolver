package Gui;

import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

public class MazePanel extends JPanel {
    float dragForce = 1.0f;
    int arrowMoveForce = 25;
    float scaleForce = 0.1f;

    Dimension minSize = new Dimension(10, 10);
    Dimension maxSize = new Dimension(100000, 100000);

    float scale = 1.0f;
    Point pos = new Point(0, 0);
    Point imgPos = new Point(0, 0);

    Point oldMousePos = null;
    Dimension oldPanelSize = new Dimension(0, 0);

    JLabel posLabel = null;
    JLabel scaleLabel = null;

    BufferedImage img;
    
    public MazePanel() {
        super();
        img = null;
    }

    public void addMoveDisplay() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(img == null) return;
                if(oldMousePos == null) {
                    oldMousePos = e.getPoint();
                    return;
                }

                imgPos.translate((int) ((e.getX() - oldMousePos.x) * dragForce), (int) ((e.getY() - oldMousePos.y) * dragForce));
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

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not needed for arrow keys
            }

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

            @Override
            public void keyReleased(KeyEvent e) {
                // Not needed for arrow keys
            }
        });
    }

    public void addScaleDisplay() {
        this.addMouseWheelListener(e -> {
            if(img == null) return;
            float newScale = scale + scale * (float) e.getWheelRotation() * scaleForce;
            //Nie pozwól na zbyt małe lub zbyt duże skalowanie
            if(newScale * img.getWidth() < minSize.width || newScale * img.getHeight() < minSize.height || newScale * img.getWidth() > maxSize.getWidth() || newScale * img.getHeight() > maxSize.getHeight()) return;
            scale = newScale;
            repaint();
        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not needed for zoom keys
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                float newScale = 0;
                switch (keyCode) {
                    case KeyEvent.VK_Z:
                        if(img == null) return;
                        newScale = scale + scale * scaleForce;
                        //Nie pozwól na zbyt małe lub zbyt duże skalowanie
                        if(newScale * img.getWidth() < minSize.width || newScale * img.getHeight() < minSize.height || newScale * img.getWidth() > maxSize.getWidth() || newScale * img.getHeight() > maxSize.getHeight()) return;
                        scale = newScale;
                        repaint();
                        break;
                    case KeyEvent.VK_X:
                        if(img == null) return;
                        newScale = scale - scale * scaleForce;
                        //Nie pozwól na zbyt małe lub zbyt duże skalowanie
                        if(newScale * img.getWidth() < minSize.width || newScale * img.getHeight() < minSize.height || newScale * img.getWidth() > maxSize.getWidth() || newScale * img.getHeight() > maxSize.getHeight()) return;
                        scale = newScale;
                        repaint();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not needed for zoom keys
            }
        });
    }

    public void updateLabels() {
        if(posLabel == null || scaleLabel == null) return;
        posLabel.setText("X: " + imgPos.x + " Y: " + imgPos.y);
        scaleLabel.setText("  Scale: " + scale + "  ");
    }

    public void addNavigationButtons() {
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        posLabel = new JLabel("X: " + pos.x + " Y: " + pos.y);
        scaleLabel = new JLabel("  Scale: " + scale + "  ");

        buttonPanel.add(posLabel);
        buttonPanel.add(scaleLabel);


        JButton rescaleButton = new JButton();
        ImageIcon serachIcon = new ImageIcon("C:\\Users\\golas\\JIMP\\MazeSolver\\src\\main\\graphics\\search.png");
        serachIcon.setImage(serachIcon.getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH));
        rescaleButton.setIcon(serachIcon);
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

    public void addInteractiveImage() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(img == null) return;
                if(e.getButton() != MouseEvent.BUTTON3) return;
                System.out.println("Mouse pressed at: " + e.getPoint());

                String[] options = {"Option 1", "Option 2"};
                Runnable[] actions = {() -> {
                    System.out.println("Option 1 selected");
                }, () -> {
                    System.out.println("Option 2 selected");
                }};
                
                

                Point mousePos = e.getPoint();
                int x = (int) ((mousePos.x - imgPos.x) / scale);
                int y = (int) ((mousePos.y - imgPos.y) / scale);

                if(img.getRGB(x, y) != Color.white.getRGB()) {
                    return;
                }

                img.setRGB(x, y, Color.GREEN.getRGB());
                repaint();
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
    }

    public void rescale() {
        if(img == null) return;
        pos.setLocation(0, 0);

        System.out.println("Panel size: " + getWidth() + "x" + getHeight());
        System.out.println("Image size: " + img.getWidth() + "x" + img.getHeight());

        //Policz komponenty tak aby obrazek był widoczny w całości
        float newScale = 1f;
        if(img.getWidth() > img.getHeight()) {
            newScale = 0.8f * (float) getWidth() / img.getWidth();
        } else {
            newScale = 0.8f * (float) getHeight() / img.getHeight();
        }

        System.out.println("New scale: " + String.valueOf(newScale));
        imgPos.setLocation(getWidth() / 2, getHeight() / 2);
        imgPos.translate((int) ( (float) img.getWidth() * newScale / -2f), (int) ( (float) img.getHeight() * newScale / -2f));

        scale = newScale;
        repaint();
    }

    public void changeImage(BufferedImage image) {
        img = image;
        rescale();
    }

    private void drawInState(Graphics g) {
        Point trueImgPos = new Point(imgPos.x + pos.x, imgPos.y + pos.y);
        g.drawImage(img, trueImgPos.x, trueImgPos.y, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale), null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(img == null) return;
        updateLabels();
        drawInState(g);
    }
}
