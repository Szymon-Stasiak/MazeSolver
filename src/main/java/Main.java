import Gui.*;

class Main {
    public static void main(String[] args) {
        Maze maze = new Maze("src/main/resources/maze.txt");
        maze.printMaze();
    }
}