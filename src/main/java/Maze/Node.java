package Maze;

import java.util.Vector;

public class Node {

    private boolean isField;

    private Node parent;

    private Vector<Node> neighbours;

    private boolean isVisited = false;

    private int x;

    private int y;

    private boolean isPath = false;

    public Node() {
        this.isField = false;
        this.parent = null;
        this.neighbours = null;
    }

    public Node(int x, int y) {
        this.isField = true;
        this.parent = null;
        this.neighbours = new Vector<Node>();
        this.x = x;
        this.y = y;
    }

    public void setField(boolean isField) {
        this.isField = isField;
    }

    public boolean getIsField() {
        return isField;
    }


    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Vector<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Vector<Node> neighbours) {
        this.neighbours = neighbours;
    }

    public boolean getIsVisited() {
        return isVisited;
    }

    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getIsPath() {
        return isPath;
    }

    public void setIsPath(boolean isPath) {
        this.isPath = isPath;
    }
}
