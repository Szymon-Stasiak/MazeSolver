package Gui;

import Maze.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

class Pixel {
    public Point pos;
    public int rgb;

    public Pixel(int x, int y, int rgb) {
        this.pos = new Point(x, y);
        this.rgb = rgb;
    }

    public Pixel(Point pos, int rgb) {
        this.pos = pos;
        this.rgb = rgb;
    }
}

public class MazePanel extends ImagePanel {
    //SETTINGS
    private boolean autoSolve = false;
    private boolean solved = false;

    //MAZE POSITIONS
    private Point startNode = null;
    private Point endNode = null;

    private Point selectPoint = null;
    private Queue<Pixel> resetRGB = null;

    //MAZE
    private Maze maze;

    public MazePanel() {
        super();
        resetRGB = new LinkedList<>();
        maze = null;
    }

    private Point nodeToPoint(Node node) {
        return new Point(node.getY(), node.getX());
    }

    private Node pointToNode(Point point) {
        if(maze == null) return null;
        return maze.getNode(point.y, point.x);
    }

    public void changeMaze(Maze maze) {
        this.maze = maze;
        solved = false;
        startNode = nodeToPoint(maze.getStart());
        endNode = nodeToPoint(maze.getEnd());
        resetRGB.clear();
        if(autoSolve) solveMaze();
        else changeImage(GuiUtilities.getInstance().mazeToImage(maze));
    }

    public void solveMaze() {
        LoadingWindow.show("Solving maze",  () -> {
            if (maze == null) return;
            maze.clearMaze();
            MazeSolver.solveMaze(pointToNode(startNode), pointToNode(endNode), maze);
            changeImage(GuiUtilities.getInstance().mazeToImage(maze));
            solved = true;
        });
    }

    //SET NEW START
    public void setStartNode(Point startNode) {
        if(solved) {
            resetRGB.add(new Pixel(this.startNode, Color.RED.getRGB()));
        } else {
            resetRGB.add(new Pixel(this.startNode, Color.WHITE.getRGB()));
        }

        this.startNode = startNode;
        if(autoSolve) solveMaze();
        else repaint();
    }

    //SET NEW END
    public void setEndNode(Point endNode) {
        if(solved) {
            resetRGB.add(new Pixel(this.endNode, Color.RED.getRGB()));
        } else {
            resetRGB.add(new Pixel(this.endNode, Color.WHITE.getRGB()));
        }

        this.endNode = endNode;
        if(autoSolve) solveMaze();
        else repaint();
    }

    //SET AUTOSOLVE
    public void setAutoSolve(boolean autoSolve) {
        this.autoSolve = autoSolve;
    }

    //ADD INTERACTIONS TO IMAGE
    public void addInteractiveMaze() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!imageLoaded())
                    return;

                if (e.getButton() != MouseEvent.BUTTON3)
                    return;

                Point mazePos = panelToPixelPos(e.getPoint());

                if (!pointToNode(mazePos).getIsField() || mazePos.equals(startNode) || mazePos.equals(endNode)){
                    return;
                }

                selectPoint = mazePos;
                repaint();

                String[] options = { "Set start", "Set end" };
                Runnable[] actions = { () -> {
                    setStartNode(mazePos);
                }, () -> {
                    setEndNode(mazePos);
                } };

                OptionMenu menu = new OptionMenu("X: " + mazePos.x + " Y: " + mazePos.y, e.getPoint(), options, actions, () -> {
                    resetRGB.add(new Pixel(selectPoint, Color.WHITE.getRGB()));
                    selectPoint = null;
                    repaint();
                });
                menu.setVisible(true);

                repaint();
            }
        });
    }

    public Point getStartNode() {
        return startNode;
    }

    public Point getEndNode() {
        return endNode;
    }

    @Override
    protected void preDraw(BufferedImage img) {
        super.preDraw(img);

        while(!resetRGB.isEmpty()) {
            Pixel p = resetRGB.poll();
            if(p == null || p.pos == null) continue;
            System.out.println("Deleting: " + p.pos);
            img.setRGB(p.pos.x, p.pos.y, p.rgb);
        }

        if(selectPoint != null) img.setRGB(selectPoint.x, selectPoint.y, Color.GREEN.getRGB());
        if(startNode != null) img.setRGB(startNode.x, startNode.y, Color.YELLOW.getRGB());
        if(endNode != null) img.setRGB(endNode.x, endNode.y, Color.BLUE.getRGB());
    }
}
