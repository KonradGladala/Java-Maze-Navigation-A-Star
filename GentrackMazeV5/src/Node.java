public class Node {
	int row;
	int col;
	//private int heuristic;
	//private int moveCost;
	//private int totalCostPerMove;
	Node parent;

	public Node(int x, int y, Node parent) {
		this.row = x;
		this.col = y;
		//this.moveCost = 0;
		this.parent = parent;
		//this.heuristic = Math.abs(row - end.row) + Math.abs(col - end.col); // - Manhattan Distance Heuristic –
		//this.moveCost += parent.moveCost + 10; // - Cost per move –
		//totalCostPerMove = moveCost + heuristic; // Sum of both to determine next best move - A star 
	}
	
	public Node(int x, int y) {
		this.row = x;
		this.col = y;
		//this.totalCostPerMove = 0;
	}

	/*public int getTotalCostPerMove() {
		return totalCostPerMove;
	}*/

	public Node() {
		this.row = 0;
		this.col = 0;
		this.parent = null;
	}

	public Node getParent() {
		return this.parent;
	}

	public String toString() {
		return "row = " + row + " col = " + col;
	}
}
