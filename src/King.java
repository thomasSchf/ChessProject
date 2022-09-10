
public class King implements Piece{
	private String pieceName = "King";
	private String pieceNameShortened = "Kg";
	private String color = "";
	private Spaces currSpace = null;
	private String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
	private boolean hasNotMoved = true;
	private CheckChecker checkChecker = null;
	
	public King(String color, ChessArrayList board, CheckChecker checkChecker) {
		this.color = color;
		this.checkChecker = checkChecker;
		pieceNameShortened = color + pieceNameShortened;

		if (color.equals("B")) {
			currSpace = board.findSpaces("e", 8);
		}
		else {
			currSpace = board.findSpaces("e", 1);
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
		if (!checkSpaces(space, board) && !castleCheck(space, board)) {return false;}
		else if (castleCheck(space, board)) {
			if (currSpace == space) {return false;}
			int letterAxisMovement = getLetterAxisMovement(space);
			int blackOrWhitePos = -1; //if black 8, white 1
			String newKingLetter = "";
			String newRookLetter = "";
			
			if (color.equals("B")) {
				blackOrWhitePos = 8;
			}
			else {
				blackOrWhitePos = 1;
			}
			
			if (letterAxisMovement > 0) {
				newKingLetter = "g";
				newRookLetter = "f";
			}
			else {
				newKingLetter = "c";
				newRookLetter = "d";
			}
			
			Spaces tempCurrSpace = currSpace;
			Piece tempKing = currSpace.getPieceOccupying();
			
			Spaces newKingSpace = board.findSpaces(newKingLetter, blackOrWhitePos);
			Spaces newRookSpace = board.findSpaces(newRookLetter, blackOrWhitePos);
			
			Piece rook = space.getPieceOccupying();
			rook.setCurrSpace(newRookSpace);
			newKingSpace.setSpace(tempKing);	
			newRookSpace.setSpace(rook);
			
			currSpace.setSpace(null);
			space.setSpace(null);
			currSpace = newKingSpace;
			
			if (!checkChecker.checkCheck(currSpace.getPieceOccupying().getPieceColor())) {
				hasNotMoved = false;
				return true;
			}
			else {
				currSpace = tempCurrSpace;
				currSpace.setSpace(newKingSpace.getPieceOccupying());
				newKingSpace.setSpace(null);
				
				rook.setCurrSpace(space);
				newRookSpace.setSpace(null); //future thomas when king is threatned castling is buggy af
				space.setSpace(rook);
				
				System.out.println("Impossible: Causes Check");
				return false;
			}
		}
		else {
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
	}
	
	@Override
	public boolean checkSpaces(Spaces space, ChessArrayList board) {
		if (space == currSpace) {return false;}
		
		int numAxisMovement = getNumAxisMovement(space);
		int letterAxisMovement = getLetterAxisMovement(space);
		
		if (Math.abs(numAxisMovement) > 1 || Math.abs(letterAxisMovement) > 1) {
			return false;
		}
		
		return true;
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
	
	private boolean castleCheck(Spaces space, ChessArrayList board) {
		Piece piece = space.getPieceOccupying();
		if (piece == null) {
			return false;
		}
		if (!piece.getPieceName().equals("Rook")) {
			return false;
		}
		if (!hasNotMoved || !piece.hasNotMovedCheck()) {
			return false;
		}
		
		int letterAxisMovement = getLetterAxisMovement(space);
		int blackOrWhitePos = -1; //if black 8, white 1
		
		if (color.equals("B")) {
			blackOrWhitePos = 8;
		}
		else {
			blackOrWhitePos = 1;
		}
		
		if (checkChecker.checkCheck(color)) {
			return false;
		}
		
		if (letterAxisMovement > 0) {
			for (int i = 5; i < letters.length - 1; ++i) {
				if (board.findSpaces(letters[i], blackOrWhitePos).getPieceOccupying() != null) {
					return false;
				}
			}
		}
		else {
			for (int i = 3; i > 0; --i) {
				if (board.findSpaces(letters[i], blackOrWhitePos).getPieceOccupying() != null) {
					return false;
				}
			}
		}
		
		return true;
	}
}
