
public class Rook implements Piece{ //Note for future thomas, may need to get the board in here so it can determine if moves are possible
	private String pieceName = "Rook";
	private String pieceNameShortened = "R";
	private Spaces currSpace = null;
	private String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
	private String color = "";
	private boolean hasNotMoved = true;
	private CheckChecker checkChecker = null;
	
	public Rook(String origin, String color, ChessArrayList board, CheckChecker checkChecker) {
		pieceNameShortened = color + origin + pieceNameShortened;
		this.checkChecker = checkChecker;
		this.color = color;
		
		if (color.equals("B")) {
			currSpace = board.findSpaces(origin, 8);
		}
		else {
			currSpace = board.findSpaces(origin, 1);
		}
	}
	
	@Override
	public String getPieceName() {
		return pieceName;
	}
	
	@Override
	public String getPieceNameShortened() {
		return pieceNameShortened;
	}
	
	@Override
	public String getPieceColor() {
		return color;
	}
	
	@Override
	public boolean move(Spaces space, ChessArrayList board) {
		if (!checkSpaces(space, board)) {return false;}
		
		if (takeSpace(space)) {
			Piece tempSpacePiece = space.getPieceOccupying();
			Spaces tempCurrSpace = currSpace;
			
			space.setSpace(currSpace.getPieceOccupying());
			currSpace.setSpace(null);
			currSpace = space;

			if (!checkChecker.checkCheck(currSpace.getPieceOccupying().getPieceColor())) {
				hasNotMoved = false;
				return true;
			}
			else {
				currSpace = tempCurrSpace;
				currSpace.setSpace(space.getPieceOccupying());
				space.setSpace(tempSpacePiece);
				
				System.out.println("Impossible: Causes Check");
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	@Override
	public boolean checkSpaces(Spaces space, ChessArrayList board) { //returns true if operation is possible
		boolean numMatch = space.getSpaceNum() == currSpace.getSpaceNum();
		boolean letterMatch = space.getSpaceChar().equals(currSpace.getSpaceChar());
		boolean pieceInWay = false;
		
		if (numMatch && letterMatch) {
			return false;
		}
		if (numMatch || letterMatch) {
			if (numMatch) {
				int startingLetterIndex = letterToIndex(currSpace.getSpaceChar());
				int endingLetterIndex = letterToIndex(space.getSpaceChar());
				
				if (endingLetterIndex > startingLetterIndex) { //if the traversal is L -> R
					for (int i = startingLetterIndex + 1; i < endingLetterIndex; ++i) {
						if (board.findSpaces(letters[i], currSpace.getSpaceNum()).getPieceOccupying() != null) {
							pieceInWay = true;
							break;
						}
					}
				}
				else {
					for (int i = startingLetterIndex - 1; i > endingLetterIndex; --i) {
						if (board.findSpaces(letters[i], currSpace.getSpaceNum()).getPieceOccupying() != null) {
							pieceInWay = true;
							break;
						}
					}
				}
			}
			if (letterMatch) {
				if (space.getSpaceNum() > currSpace.getSpaceNum()) {
					for (int i = currSpace.getSpaceNum() + 1; i < space.getSpaceNum(); ++i) {
						if (board.findSpaces(currSpace.getSpaceChar(), i).getPieceOccupying() != null) {
							pieceInWay = true;
							break;
						}
					}
				}
				else {
					for (int i = currSpace.getSpaceNum() - 1; i > space.getSpaceNum(); --i) {
						if (board.findSpaces(currSpace.getSpaceChar(), i).getPieceOccupying() != null) {
							pieceInWay = true;
							break;
						}
					}
				}
			}
			
			return !pieceInWay;
		}
		else {
			return false;
		}
	}
	
	private int letterToIndex(String letter) {
		int indexNum = -1;
		
		for (int i = 0; i < letters.length; ++i) {
			if (letter.equals(letters[i])) {
				indexNum = i;
			}
		}
		
		if (indexNum == -1) {
			throw new IllegalArgumentException();
		}
		else {
			return indexNum;
		}
	}
	
	@Override
	public boolean takeSpace(Spaces fightSpace) {
		if (fightSpace.getPieceOccupying() == null) {return true;}
		if (fightSpace.getPieceOccupying().getPieceNameShortened().substring(0, 1).equals(pieceNameShortened.substring(0, 1)) ) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean checkPawnIfTwoSpaceMovePrevTurn() {
		return false;
	}
	
	@Override
	public boolean hasNotMovedCheck() {
		return hasNotMoved;
	}
	
	@Override
	public Spaces getPieceSpace() {
		return currSpace;
	}
	
	@Override
	public void setCurrSpace(Spaces currSpace) {
		this.currSpace = currSpace; 
	}
}
