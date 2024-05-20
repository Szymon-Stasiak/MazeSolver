package Gui;

import Maze.Maze;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class GuiUtilities {
    public static GuiUtilities instance;

    private int saveScale = 10;

    BufferedImage mazeToImage(Maze maze) {
        int width = maze.getMaze().get(0).size();
        int height = maze.getMaze().size();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                
                if (maze.getMaze().get(i).get(j).getIsPath()) {
                    img.setRGB(j, i, 0xFF0000);
                } else if(maze.getMaze().get(i).get(j).getIsField()) {
                    img.setRGB(j, i, 0xFFFFFF);
                } else {
                    img.setRGB(j, i, 0x000000);
                }
            }
        }
        return img;
    }

    public BufferedImage scaleToSave(BufferedImage img) {
        Image scaleImg = img.getScaledInstance(img.getWidth() * saveScale, img.getHeight() * saveScale, BufferedImage.SCALE_REPLICATE);

        BufferedImage bimage = new BufferedImage(img.getWidth(null) * saveScale, img.getHeight(null) * saveScale, BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(scaleImg, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    public synchronized static GuiUtilities getInstance() {
        if (instance == null) {
            instance = new GuiUtilities();
        }
        return instance;
    }
}
