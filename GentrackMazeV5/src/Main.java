import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/** 
 * Breadth First Search Algorithm implementation in Java.
 * This program implements BFS algorithm on a 2D int array abstracted to a Maze. 
 * Maze data is stored in a text.file in the following format:
 * ----------------
 * 6 5 - Maze dimensions (width and height).
 * 1 3 - Start location (width and height).
 * 3 3 - End location (width and height)
 * 1 1 1 1 1 1
 * 0 0 1 0 0 0
 * 1 0 1 0 1 1
 * 1 0 1 0 1 1
 * 1 1 1 1 1 1
 * 1s are considered walls and 0s are open areas. Additionally, maze can wrap around itself if the edges on both sides are 0s. 
 * ----------------
 * This program utilises BFS algorithm to build the optimal path through the maze, exploring every possibility.
 * The shortest path through the maze should always be returned. 
 * 
 * Upon running, file location will be requested this should be a direct file path such as: C:\Users\User\Desktop\Folder\Folder\file.txt  
 * @Author Konrad Gladala
 * @JavaVersion "10.0.2" 2018-07-17
 * 
*/
public class Main {
	//Constants 
	public static final int EMPTY_CELl = 0;
	public static final int END_CELL = 9;
	public static final int VISITED_CELL = -1;
	public static final int WALL_CELL = 1;	
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter exact maze file location: ");
		String fileLocation = reader.nextLine(); // Example of valid input: C:\Users\Hinnene\Desktop\Maze\Samples\input.txt
		reader.close();

		ArrayList<String> mazeRaw = new ArrayList<String>(); 
		File file = new File(fileLocation); 

		try {
			String st;
			BufferedReader br = new BufferedReader(new FileReader(file)); //Read the file
			while ((st = br.readLine()) != null) //Extract all lines and add them to a ArrayList<String>.
				mazeRaw.add(st);
			br.close();
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

		Maze testMaze = new Maze(mazeRaw); // 2D Int array maze constructor, will construct a 2Dmaze with a valid String ArrayList Input.
		if (testMaze.getMaze2D()[0].length > 2 && testMaze.getMaze2D().length > 2) { //Not necessary can be removed, but maze should be at least 3 by 3.  
			Node p = solveMaze(testMaze); // Solves the maze.
			testMaze.printMazeResult(p); // Prints the maze from Node parents.
		}
	}

	/** This method solves the maze by utilising a Queue holding all the Nodes to be explored. 
	 * Starting location is fed to the queue then a while loop starts and extracts each Node element of the queue one by one putting it through all the checks to generate new Nodes to check. 
	 * @param Maze - This parameter is a Maze object which contains a 2Dint array 
	 * @return Node -  returns a Node which holds the previous Nodes. Path can be reconstructed from this Node by extracting its parents.
	 * */
	public static Node solveMaze(Maze maze) { 
	    Queue<Node> queue = new LinkedList<Node>();   
		queue.add(maze.getStart()); // Adds the starting coordinates to the queue.
		while (!queue.isEmpty()) // Continue the while loop until exit found or maze is returned as null which means unsolvable.
		{
			Node currentPoint = queue.remove(); //Assign the top Node from the queue to a local value for use.
			maze.incCounter(); //Increment an private Int value in the Maze Class for display purposes. 
	
			//If ending found return the Node with the path through maze in its parent Nodes defined in the Node Class constructor.
			if (maze.getMaze2D()[currentPoint.row][currentPoint.col] == END_CELL) {
				return currentPoint;
			}
			
			if(maze.getMaze2D()[currentPoint.row][currentPoint.col] != VISITED_CELL) //If the current Node was not visited check its neighbours. 
			{
			tryMove(maze, currentPoint, queue, -1, 0); // South
			tryMove(maze, currentPoint, queue, +1, 0); // North
			tryMove(maze, currentPoint, queue, 0, -1); // West
			tryMove(maze, currentPoint, queue, 0, +1); // East
			}
		}
		return null; // If maze cannot be solved value null is returned;
	}
		
	static void tryMove(Maze maze, Node point, Queue<Node> q, int dx, int dy) {
	    int x = point.row + dx; //offset of row.    
	    int y = point.col + dy; //offset of column.   
	   
	    //Confirm if the current Node is a valid wrap candidate and that the previous Node has not been wrapped (to avoid loops going back and fourth).
	    if (isWrappable(maze, point.row, point.col, point).size() != 0 && isWrappable(maze, point.parent.row, point.parent.col, point.parent).size() == 0) 
			if(isWrappable(maze, point.row, point.col, point).size() > 1) { //If you are at one of the 4 corners of the maze add both Vertical and Horizontal wrap (if both are valid) - Very rare case but must be checked. 
	    	q.add(isWrappable(maze, point.row, point.col, point).get(0));
	        q.add(isWrappable(maze, point.row, point.col, point).get(1)); 
	        }
	        else q.add(isWrappable(maze, point.row, point.col, point).get(0)); //Add the exit Node given by the isWrappable function to the Queue. 
	   
	    //Attempt to move in a offset direction if a validity check performed by isFree is true.
	    if (isFree(maze, x, y)) {
	        maze.getMaze2D()[point.row][point.col] = VISITED_CELL; //Set the current node to visited.
	        q.add(new Node(x, y, point)); //Add the offset Node to the Queue.
	    }
	}

	//isFree performs all the validity checks before a node can be added to a Queue. 
	public static boolean isFree(Maze maze, int x, int y) {
		if ((x >= 0 && x <= maze.getRow()) && (y >= 0 && y <= maze.getColumn()) && (maze.getMaze2D()[x][y] == EMPTY_CELl || maze.getMaze2D()[x][y] == END_CELL)) //Has to be a valid index and either an empty cell or end cell.
			return true; //If checks passed return true.
			return false; //Otherwise return false.
	}
			
	//Checks if a coordinate can be wrapped or not.
	public static ArrayList<Node> isWrappable(Maze maze, int row, int col, Node parent) {
		    ArrayList<Node> result = new ArrayList<Node>();
		    if(row == 0 && maze.getMaze2D()[maze.getRow()][col] != WALL_CELL) result.add(new Node(maze.getRow(), col, parent)); // Vertical - Check if the Node on the other side is not a wall then - Come out on the Bottom. 
		    if(row == maze.getRow() && maze.getMaze2D()[0][col] != WALL_CELL) result.add(new Node(0, col, parent)); // Vertical - Check if the Node on the other side is not a wall then - Come out on the Top.
		    if(col == 0 && maze.getMaze2D()[row][maze.getColumn()] != WALL_CELL) result.add(new Node(row, maze.getColumn(), parent)); // Horizontal - Check if the Node on the other side is not a wall then - Come out on Right.
		    if(col == maze.getColumn() && maze.getMaze2D()[row][0] != WALL_CELL) result.add(new Node(row, 0, parent)); // Horizontal - Check if the Node on the other side is not a wall then - Come out on Left.
		    return result; // Return result, if result size is 0 then the coordinate cannot be wrapped.
	}
}
