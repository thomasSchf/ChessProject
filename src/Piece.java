
public interface Piece {
	public abstract String getPieceName(); //returns the name of the piece
	public abstract String getPieceNameShortened();
	public abstract String getPieceColor(); //late addition try to bring this to others;
	
	public abstract boolean move(Spaces space, ChessArrayList board); //returns true/false if move completed or was valid
	
	public abstract boolean checkSpaces(Spaces space, ChessArrayList board); //checks if a given space is movable by a space
	
	public abstract boolean takeSpace(Spaces fightSpace); //checks if the target piece is an opponent
	
	public abstract boolean checkPawnIfTwoSpaceMovePrevTurn();
	
	public abstract boolean hasNotMovedCheck();
	
	public abstract Spaces getPieceSpace();
	
	public abstract void setCurrSpace(Spaces currSpace);
}
