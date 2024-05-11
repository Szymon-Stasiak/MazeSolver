import Maze.Maze;
import Maze.MazeSolver;

class Main {
    public static void main(String[] args) {
        Maze maze = new Maze("src/main/resources/maze.txt");
        //maze.printMaze();
        MazeSolver.solveMaze(maze.getStart(), maze.getEnd(), maze);
    }
}