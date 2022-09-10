
public class Spaces {
	private String spaceChar = "";
	private int spaceNum = -1;
	private Piece pieceOccupying = null;
	
	public Spaces(String spaceChar, int spaceNum) {
		this.spaceNum = spaceNum;
		this.spaceChar = spaceChar;
	}
	
	public void setSpace(Piece newPiece) {
		pieceOccupying = newPiece;
	}
	
	public String getSpaceChar() {
		return spaceChar;
	}
	
	public int getSpaceNum() {
		return spaceNum;
	}
	
	public Piece getPieceOccupying() {
		return pieceOccupying;
	}
}
