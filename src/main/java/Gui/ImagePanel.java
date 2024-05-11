package Gui;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    float dragForce = 1.0f;
    float scaleForce = 0.01f;

    Dimension minSize = new Dimension(10, 10);
    Dimension maxSize = new Dimension(10000, 10000);

    float scale = 1.0f;
    Point pos = new Point(0, 0);
    Point imgPos = new Point(0, 0);

    Point oldMousePos = null;
    Dimension oldPanelSize = new Dimension(0, 0);

    BufferedImage img;

    public ImagePanel() {
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
    }

    public void addScaleDisplay() {
        this.addMouseWheelListener(e -> {
            if(img == null) return;
            float newScale = scale + (float) e.getWheelRotation() * scaleForce;
            //Nie pozwól na zbyt małe lub zbyt duże skalowanie
            if(newScale * img.getWidth() < minSize.width || newScale * img.getHeight() < minSize.height || newScale * img.getWidth() > maxSize.getWidth() || newScale * img.getHeight() > maxSize.getHeight()) return;
            scale = newScale;
            repaint();
        });
    }

    public ImagePanel(BufferedImage image) {
        super();
        img = image;
    }

    public void changeImage(BufferedImage image) {
        img = image;
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

    private void drawInState(Graphics g) {
        Point trueImgPos = new Point(imgPos.x + pos.x, imgPos.y + pos.y);
        g.drawImage(img, trueImgPos.x, trueImgPos.y, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale), null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(img == null) return;
        drawInState(g);
    }
}
