import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Maze {
	private int[][] maze2D; //
	private Node start;
	private Node end;
	private int moveCounter;
	private int solutionCounter;

	public Maze(ArrayList<String> mazeString ) {
		String[] dimensionData = mazeString.get(0).split(" ");
		int row = Integer.valueOf(dimensionData[1]);
		int column = Integer.valueOf(dimensionData[0]);

		this.maze2D = new int[row][column]; // Assign maze size from the 1st line in file.
		this.start = new Node(Integer.valueOf(mazeString.get(1).split(" ")[1]),
				Integer.valueOf(mazeString.get(1).split(" ")[0])); // Assign starting position from 2nd line.
		this.end = new Node(Integer.valueOf(mazeString.get(2).split(" ")[1]),
				Integer.valueOf(mazeString.get(2).split(" ")[0])); // Assign end position from 3rd line.

		for (int i = 0; i < this.maze2D.length; i++) {
			for (int n = 0; n < this.maze2D[i].length; n++) {
				this.maze2D[i][n] = Integer.valueOf(mazeString.get(i + 3).split(" ")[n]); // i + 3 to offset first 3
																							// lines of text file. Then
																							// assign at each n index
																							// either 1 or 0 height of maze read from file.
			}
		}
		this.maze2D[end.row][end.col] = 9; // Set the end coordinates in maze, if not allowed to modify maze file this
											// can be removed and Main Class changed to use maze.getEnd() instead. Also
											// for solving the maze, clone the object before modifing it if not allowed
											// to.
	}
	
	// Print result using a 2D String Array of the same size as the Maze.
	public void printMazeResult(Node p) {
		String[][] results = new String[this.getMaze2D().length][this.getMaze2D()[0].length];
		Boolean mazeIsSolved = p != null;
		// Assign values to string array based on the finished maze state values.
		for (int i = 0; i < getMaze2D().length; i++) {
			for (int n = 0; n < getMaze2D()[i].length; n++) {
				switch (maze2D[i][n]) {
				case 0:
					results[i][n] = " ";
					break;
				case 1:
					results[i][n] = "#";
					break;
				case -1:
					results[i][n] = " ";
					break;
				case 9:
					results[i][n] = "E";
					break;
				}
			}
		}

		// Extract the Node parents and rebuild the path overwriting the results array
		// to build a path for display.
		try {
			while (p.getParent() != null) {
				p = p.getParent();
				if (maze2D[p.row][p.col] == 9)
					break;
				results[p.row][p.col] = "X";
				solutionCounter++;
			}
			results[start.row][start.col] = "S";
		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
		}

		// Display the results.
		for (int i = 0; i < results.length; i++) {
			for (int n = 0; n < results[i].length; n++) {
				System.out.print(results[i][n]);
			}
			System.out.println();
		}

		// Display the maze status.
		if (mazeIsSolved)
			System.out.printf("Maze was solved, solution reached in %d moves. Total number of valid operations: %d",
					solutionCounter, moveCounter);
		else
			System.out.printf("- Maze is unsolvable, Total number of valid operations: %d-", moveCounter);
	}

	//Getters and Setters 
	
	public void incCounter() {
		moveCounter++;
	}

	public int getRow() {
		// TODO Auto-generated method stub
		return this.maze2D.length - 1;
	}

	public int getColumn() {
		// TODO Auto-generated method stub
		return this.maze2D[0].length - 1;
	}

	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}

	public int[][] getMaze2D() {
		return maze2D;
	}

	public void setMaze2D(int[][] maze2d) {
		maze2D = maze2d;
	}
	
}
