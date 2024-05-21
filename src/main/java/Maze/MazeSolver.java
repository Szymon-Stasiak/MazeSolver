package Maze;

import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver {

    public static boolean solveMaze(int startX, int startY, int endX, int endY, Maze maze) {
        Node start = maze.getMaze().get(startX).get(startY);
        Node end = maze.getMaze().get(endX).get(endY);
        return solveMaze(start, end, maze);
    }

    public static boolean solveMaze(Node start, Node end, Maze maze) {
        makeParentBFS(end, start);
        return  markPath(start, end);
    }

    public static void makeParentBFS(Node start, Node end) {

        Queue<Node> queue = new LinkedList<>();
        queue.add(start);
        start.setIsVisited(true);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current == end) {
                break;
            }
            for (Node neighbour : current.getNeighbours()) {
                if (!neighbour.getIsVisited()) {
                    queue.add(neighbour);
                    neighbour.setIsVisited(true);
                    neighbour.setParent(current);
                }
            }
        }
    }

 public static boolean  markPath(Node start, Node end) {
        Node current = start;
        while (current != end) {
            if (current == null) {
                return false;
            }
            current.setIsPath(true);
            current = current.getParent();
        }
        end.setIsPath(true);
        return true;
    }


}

