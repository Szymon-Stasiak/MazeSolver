import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Maze {

    private Vector<Vector<Node>> maze;

   public Maze(String filename) {
        File file = new File(filename);
        maze = new Vector<Vector<Node>>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                maze.add(new Vector<Node>());
                String line = scanner.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == 'X') {
                        maze.lastElement().add(new Node(false));
                    } else {
                        maze.lastElement().add(new Node());
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + filename);
        }
    }

    //test
    public void printMaze() {
        for (Vector<Node> row : maze) {
            for (Node node : row) {
                if (node.getIsField()) {
                    System.out.print(" ");
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }




}
