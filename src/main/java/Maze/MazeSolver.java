package Maze;

import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver {

    public static void soleMaze(int startX, int startY, int endX, int endY, Maze maze) {
        Node start = maze.getMaze().get(startX).get(startY);
        Node end = maze.getMaze().get(endX).get(endY);
        solveMaze(start, end, maze);
    }

    public static void solveMaze(Node start, Node end, Maze maze) {
        makeParentBFS(end, start);
        printPath(start, end);
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

    public static void printPath(Node start, Node end) {
        Node current = start;

        System.out.println(end.getX() + " " + start.getY());

        while (current != end) {
            System.out.println(current.getX() + " " + current.getY());
            current.setIsPath(true);
            current = current.getParent();
        }
        System.out.println(end.getX() + " " + end.getY());
        end.setIsPath(true);

    }

    public static void setPath(Node start, Node end) {
        Node current = start;

        while (current != end) {
            current.setIsPath(true);
            current = current.getParent();
        }
        end.setIsPath(true);
    }

}

