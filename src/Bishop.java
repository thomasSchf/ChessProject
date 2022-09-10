
public class Bishop implements Piece{
	private String pieceName = "Bishop";
	private String pieceNameShortened = "B";
	private Spaces currSpace = null;
	private String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
	private String color = "";
	private boolean hasNotMoved = true;
	private CheckChecker checkChecker = null;
	
	public Bishop(String origin, String color, ChessArrayList board, CheckChecker checkChecker) {
		this.color = color;
		this.checkChecker = checkChecker;
		pieceNameShortened = color + origin + pieceNameShortened;
		
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
	public boolean checkSpaces(Spaces space, ChessArrayList board) {
		int numAxisMovement = getNumAxisMovement(space);
		int letterAxisMovement = getLetterAxisMovement(space);
		boolean isDiagonal = Math.abs(numAxisMovement) == Math.abs(letterAxisMovement);
		
		if (numAxisMovement == 0 || letterAxisMovement == 0) {
			return false;
		}
		if (!isDiagonal) {
			return false;
		}
		
		int navNum = -1;
		int navLetter = -1;
		int goalNum = space.getSpaceNum();
		int goalLetter = letterToIndex(space.getSpaceChar());
		
		if (numAxisMovement > 0 && letterAxisMovement > 0) { //Up Right
			navNum = currSpace.getSpaceNum() + 1;
			navLetter = letterToIndex(currSpace.getSpaceChar()) + 1;
			
			while (navNum < goalNum && navLetter < goalLetter) {
				if (board.findSpaces(letters[navLetter], navNum).getPieceOccupying() != null) {
					return false;
				}
				
				++navNum;
				++navLetter;
			}
			
			return true;
		}
		else if (numAxisMovement > 0 && letterAxisMovement < 0) { //Up Left
			navNum = currSpace.getSpaceNum() + 1;
			navLetter = letterToIndex(currSpace.getSpaceChar()) - 1;
			
			while (navNum < goalNum && navLetter > goalLetter) {
				if (board.findSpaces(letters[navLetter], navNum).getPieceOccupying() != null) {
					return false;
				}
				
				++navNum;
				--navLetter;
			}
			
			return true;
		}
		else if (numAxisMovement < 0 && letterAxisMovement < 0) { //Down Left
			navNum = currSpace.getSpaceNum() - 1;
			navLetter = letterToIndex(currSpace.getSpaceChar()) - 1;
			
			while (navNum > goalNum && navLetter > goalLetter) {
				if (board.findSpaces(letters[navLetter], navNum).getPieceOccupying() != null) {
					return false;
				}
				
				--navNum;
				--navLetter;
			}
			
			return true;
		}
		else { //Down Right
			navNum = currSpace.getSpaceNum() - 1;
			navLetter = letterToIndex(currSpace.getSpaceChar()) + 1;
			
			while (navNum > goalNum && navLetter < goalLetter) {
				if (board.findSpaces(letters[navLetter], navNum).getPieceOccupying() != null) {
					return false;
				}
				
				--navNum;
				++navLetter;
			}
			
			return true;
		}
	}
	
	@Override
	public boolean takeSpace(Spaces fightSpace) {
		if (fightSpace.getPieceOccupying() == null) {return true;}
		if (fightSpace.getPieceOccupying().getPieceColor().equals(color)) {
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
	
	private int getNumAxisMovement(Spaces space) {
		return space.getSpaceNum() - currSpace.getSpaceNum();
	}
	
	private int getLetterAxisMovement(Spaces space) {
		int firstLetterNum = 0;
		int secondLetterNum = 0;
		
		for (int i = 0; i < letters.length; ++i) {
			if (currSpace.getSpaceChar().equals(letters[i])) {
				firstLetterNum = i;
			}
			if (space.getSpaceChar().equals(letters[i])) {
				secondLetterNum = i;
			}
		}
		
		return secondLetterNum - firstLetterNum;
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
}
