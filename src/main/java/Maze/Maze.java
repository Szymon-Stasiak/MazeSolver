package Maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Maze {

    private final Vector<Vector<Node>> maze;

    private Node start;

    private Node end;

    public Maze(String filename) {
        File file = new File(filename);
        maze = new Vector<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                maze.add(new Vector<>());
                String line = scanner.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == 'X') {
                        maze.lastElement().add(new Node());
                    } else if (line.charAt(i) == 'P') {
                        maze.lastElement().add(new Node(maze.size() - 1, i));
                        start = maze.lastElement().lastElement();
                    } else if (line.charAt(i) == 'K') {
                        maze.lastElement().add(new Node(maze.size() - 1, i));
                        end = maze.lastElement().lastElement();
                    } else {
                        maze.lastElement().add(new Node(maze.size() - 1, i));
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + filename);
        }
        setNeighbours();
    }

    private void setNeighbours() {
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(i).size(); j++) {
                if (maze.get(i).get(j).getIsField()) {
                    if (i > 0 && maze.get(i - 1).get(j).getIsField()) {
                        maze.get(i).get(j).getNeighbours().add(maze.get(i - 1).get(j));
                    }
                    if (i < maze.size() - 1 && maze.get(i + 1).get(j).getIsField()) {
                        maze.get(i).get(j).getNeighbours().add(maze.get(i + 1).get(j));
                    }
                    if (j > 0 && maze.get(i).get(j - 1).getIsField()) {
                        maze.get(i).get(j).getNeighbours().add(maze.get(i).get(j - 1));
                    }
                    if (j < maze.get(i).size() - 1 && maze.get(i).get(j + 1).getIsField()) {
                        maze.get(i).get(j).getNeighbours().add(maze.get(i).get(j + 1));
                    }
                }
            }
        }
    }

    //test
    public void printMaze() {
        for (Vector<Node> row : maze) {
            for (Node node : row) {
                if (node.getIsField()) {
                    System.out.print(" ");
                    //System.out.print(node.getNeighbours().size());
                    //System.out.print(node.getX()+ " " + node.getY());
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }


    public Vector<Vector<Node>> getMaze() {
        return maze;
    }

    public Node getNode(int x, int y) {
        return maze.get(x).get(y);
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }


}
