import java.util.Vector;

public class Node {

    private boolean isField ;

    private Node parent;

    private Vector<Node> neighbours;

    public Node() {
        this.isField = true;
        this.parent = null;
        this.neighbours = new Vector<Node>();
    }

    public Node(boolean isField) {
        this.isField = isField;
        this.parent = null;
        this.neighbours = new Vector<Node>();
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

}
