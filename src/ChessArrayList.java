
public class ChessArrayList {
	private Node[] numberAxis = new Node[8]; //tried generic class decided on spaces only
	
	public ChessArrayList() { //Constructor
		for (int i = 0; i < 8; ++i) {
			numberAxis[i] = new Node(i + 1, "d");
			//make it so that if it isnt 0 it adds it to the pile
			organizeSpaceChars(numberAxis[i], i + 1);
		}
	}
	
	private void organizeSpaceChars(Node root, int spaceNum) {
		root.addNode("b", spaceNum);
		root.addNode("a", spaceNum);
		root.addNode("c", spaceNum);
		root.addNode("f", spaceNum);
		root.addNode("e", spaceNum);
		root.addNode("g", spaceNum);
		root.addNode("h", spaceNum);
	}
	
	public Spaces findSpaces(String spaceChar, int spaceNum) {
		if (spaceChar.compareTo("a") < 0 || spaceChar.compareTo("h") > 0) {
			throw new IllegalArgumentException("tried to find an invalid letter space");
		}
		return numberAxis[spaceNum - 1].findSpaces(spaceChar); //Need to get number and letter, navigate to number, ship it with lettter to node
	}
	
	public class Node {
		//public Node head = null; //probably unneeded
		public Node left = null; //public because it is a class within a class
		public Node right = null;
		public Spaces space = null;
		
		public Node(int spaceNum, String spaceChar) {
			space = new Spaces(spaceChar, spaceNum);
		}
		
		public Spaces findSpaces(String spaceChar) {
			if (spaceChar.compareTo(space.getSpaceChar()) == 0) {
				return space;
			}
			else if (spaceChar.compareTo(space.getSpaceChar()) < 0) {
				if (left == null) {
					throw new IndexOutOfBoundsException("find space attempted to find a space outside of a-h range");
				}
				else {
					return left.findSpaces(spaceChar); //recursion example
				}
			}
			else { //to save operations just assumes that right is the only possible answer left.
				if (right == null) {
					throw new IndexOutOfBoundsException("find space attempted to find a space outside of a-h range");
				}
				else {
					return right.findSpaces(spaceChar);
				}
			}			
		}
		
		public void addNode(String spaceChar, int spaceNum) {
			if (spaceChar.compareTo(space.getSpaceChar()) < 0) {
				if (left == null) {
					left = new Node(spaceNum, spaceChar);
				}
				else {
					left.addNode(spaceChar, spaceNum);
				}
			}
			else if (spaceChar.compareTo(space.getSpaceChar()) > 0) {
				if (right == null) {
					right = new Node(spaceNum, spaceChar);
				}
				else {
					right.addNode(spaceChar, spaceNum);
				}
			}
			else {
				throw new IndexOutOfBoundsException("Somehow got a repeat letter when setting up chess array LIst");
			}
		}
	}
}
