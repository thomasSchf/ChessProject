import java.util.Scanner;

public class Pawn implements Piece{
	private Scanner scnr = new Scanner(System.in);
	private String pieceName = "Pawn";
	private String pieceNameShortened = "P";
	private Spaces currSpace = null;
	private String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
	private String color = "";
	private boolean twoSpaceMovePossible = true;
	private boolean twoSpaceMovePrevTurn = false;
	private CheckChecker checkChecker = null;
	
	public Pawn(String origin, String color, ChessArrayList board, CheckChecker checkChecker) {
		this.color = color;
		this.checkChecker = checkChecker;
		pieceNameShortened = color + origin + pieceNameShortened;
		
		if (color.equals("B")) {
			currSpace = board.findSpaces(origin, 7);
		}
		else {
			currSpace = board.findSpaces(origin, 2);
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
		if (!checkSpaces(space, board)) {//dont forget to set previous space null
			return false;
		}
		int numAxisMovement = getNumAxisMovement(space, board);
		int letterAxisMovement = getLetterAxisMovement(space, board);
		
		if (Math.abs(numAxisMovement) == 2) {
			Piece tempSpacePiece = space.getPieceOccupying();
			Spaces tempCurrSpace = currSpace;
			
			space.setSpace(currSpace.getPieceOccupying());
			currSpace.setSpace(null);
			currSpace = space;
			
			if (!checkChecker.checkCheck(currSpace.getPieceOccupying().getPieceColor())) {
				twoSpaceMovePossible = false;
				twoSpaceMovePrevTurn = true;
			}
			else {
				currSpace = tempCurrSpace;
				currSpace.setSpace(space.getPieceOccupying());
				space.setSpace(tempSpacePiece);
				
				System.out.println("Impossible: Causes Check");
				return false;
			}
		}
		else if (Math.abs(numAxisMovement) == 1 && Math.abs(letterAxisMovement) == 1 && space.getPieceOccupying() == null) {
			Spaces adjacentSpace = null;
			
			if (letterAxisMovement > 0) {
				adjacentSpace = board.findSpaces(letters[letterToIndex(currSpace.getSpaceChar()) + 1], currSpace.getSpaceNum());
			}
			else {
				adjacentSpace = board.findSpaces(letters[letterToIndex(currSpace.getSpaceChar()) - 1], currSpace.getSpaceNum());
			}
			
			if (takeSpace(adjacentSpace)) {		
				Piece tempAdjacentPiece = adjacentSpace.getPieceOccupying();
				Spaces tempCurrSpace = currSpace;
				
				adjacentSpace.setSpace(null);
				space.setSpace(currSpace.getPieceOccupying());
				currSpace.setSpace(null);
				currSpace = space;
				
				if (!checkChecker.checkCheck(currSpace.getPieceOccupying().getPieceColor())) {
					twoSpaceMovePossible = false;
					twoSpaceMovePrevTurn = false;
				}
				else {
					currSpace = tempCurrSpace;
					currSpace.setSpace(space.getPieceOccupying());
					space.setSpace(null);
					adjacentSpace.setSpace(tempAdjacentPiece);
					
					System.out.println("Impossible: Causes Check");
					return false;
				}
			}
			else {
				return false;
			}
		}
		else if (Math.abs(numAxisMovement) == 1 && Math.abs(letterAxisMovement) == 1) {
			if (takeSpace(space)) {
				Piece tempSpacePiece = space.getPieceOccupying();
				Spaces tempCurrSpace = currSpace;
				
				space.setSpace(currSpace.getPieceOccupying());
				currSpace.setSpace(null);
				currSpace = space;
				
				if (!checkChecker.checkCheck(currSpace.getPieceOccupying().getPieceColor())) {
					twoSpaceMovePossible = false;
					twoSpaceMovePrevTurn = false;
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
		else if (Math.abs(numAxisMovement) == 1) {
			Piece tempSpacePiece = space.getPieceOccupying();
			Spaces tempCurrSpace = currSpace;
			
			space.setSpace(currSpace.getPieceOccupying());
			currSpace.setSpace(null);
			currSpace = space;
			
			if (!checkChecker.checkCheck(currSpace.getPieceOccupying().getPieceColor())) {
				twoSpaceMovePossible = false;
				twoSpaceMovePrevTurn = false;
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
		
		transformPawn(board);
		return true;
	}
	
	@Override
	public boolean checkSpaces(Spaces space, ChessArrayList board) {
		//if the space is in front, straight 1 times, striaght 2 for first move, or enpassant is possible / piece is there
		//Black decreasing number, White increasing
		if (color.equals("B")) {
			if (space.getSpaceNum() < currSpace.getSpaceNum()) {
				int numAxisMovement = getNumAxisMovement(space, board);
				int letterAxisMovement = getLetterAxisMovement(space, board);
				
				if (numAxisMovement >= 0 || Math.abs(numAxisMovement) > 2 || Math.abs(letterAxisMovement) > 1) {
					return false;	//If it moves in the wrong direction or more spaces then possible
				}
				if (Math.abs(numAxisMovement) == 2 && Math.abs(letterAxisMovement) > 0) {
					return false;
				}
				if (Math.abs(numAxisMovement) == 2 && !twoSpaceMovePossible) {
					return false;
				}
				if (Math.abs(numAxisMovement) == 2) {
					for (int i = currSpace.getSpaceNum() - 1; i >= space.getSpaceNum(); --i) {
						if (board.findSpaces(currSpace.getSpaceChar(), i).getPieceOccupying() != null) {
							return false;
						}
					}
					
					return true; //FIXME check if this works out
				}
				if (Math.abs(numAxisMovement) == 1 && Math.abs(letterAxisMovement) == 1) {
					Piece targetPiece = space.getPieceOccupying();
					Piece adjacentPiece = null;
					
					if (targetPiece == null) {
						if (letterAxisMovement > 0) {
							adjacentPiece = board.findSpaces(letters[letterToIndex(currSpace.getSpaceChar()) + 1], currSpace.getSpaceNum()).getPieceOccupying();
						}
						else {
							adjacentPiece = board.findSpaces(letters[letterToIndex(currSpace.getSpaceChar()) - 1], currSpace.getSpaceNum()).getPieceOccupying();
						}
						
						if (adjacentPiece == null) {
							return false;
						}
						if (!adjacentPiece.checkPawnIfTwoSpaceMovePrevTurn()) {
							return false;
						}
					}
					
					return true;
				}
				if (Math.abs(numAxisMovement) == 1) {
					if (board.findSpaces(currSpace.getSpaceChar(), currSpace.getSpaceNum() - 1).getPieceOccupying() != null) {
						return false;
					}
					
					return true;
				}
				
				return false; //tired rn this may not be reachable
			}
			else {
				return false;
			}
		}
		else {
			if (space.getSpaceNum() > currSpace.getSpaceNum()) {
				int numAxisMovement = getNumAxisMovement(space, board);
				int letterAxisMovement = getLetterAxisMovement(space, board);
				
				if (numAxisMovement <= 0 || Math.abs(numAxisMovement) > 2 || Math.abs(letterAxisMovement) > 1) {
					return false;	//If it moves in the wrong direction or more spaces then possible
				}
				if (Math.abs(numAxisMovement) == 2 && Math.abs(letterAxisMovement) > 0) {
					return false;
				}
				if (Math.abs(numAxisMovement) == 2 && !twoSpaceMovePossible) {
					return false;
				}
				if (Math.abs(numAxisMovement) == 2) {
					for (int i = currSpace.getSpaceNum() + 1; i <= space.getSpaceNum(); ++i) {
						if (board.findSpaces(currSpace.getSpaceChar(), i).getPieceOccupying() != null) {
							return false;
						}
					}
					
					return true; //FIXME check if this works out
				}
				if (Math.abs(numAxisMovement) == 1 && Math.abs(letterAxisMovement) == 1) {
					Piece targetPiece = space.getPieceOccupying();
					Piece adjacentPiece = null;
					
					if (targetPiece == null) {
						if (letterAxisMovement > 0) {
							adjacentPiece = board.findSpaces(letters[letterToIndex(currSpace.getSpaceChar()) + 1], currSpace.getSpaceNum()).getPieceOccupying();
						}
						else {
							adjacentPiece = board.findSpaces(letters[letterToIndex(currSpace.getSpaceChar()) - 1], currSpace.getSpaceNum()).getPieceOccupying();
						}
						
						if (adjacentPiece == null) {
							return false;
						}
						if (!adjacentPiece.checkPawnIfTwoSpaceMovePrevTurn()) {
							return false;
						}
					}
					
					return true;
				}
				if (Math.abs(numAxisMovement) == 1) {
					if (board.findSpaces(currSpace.getSpaceChar(), currSpace.getSpaceNum() + 1).getPieceOccupying() != null) {
						return false;
					}
					
					return true;
				}
				
				return false;
			}
			else {
				return false;
			}
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
		return twoSpaceMovePrevTurn;
	}
	
	@Override
	public boolean hasNotMovedCheck() {
		return twoSpaceMovePossible; //essentially the same thing as not moving
	}
	
	@Override
	public Spaces getPieceSpace() {
		return currSpace;
	}
	
	@Override
	public void setCurrSpace(Spaces currSpace) {
		this.currSpace = currSpace; 
	}
	
	private int getNumAxisMovement(Spaces space, ChessArrayList board) {
		return space.getSpaceNum() - currSpace.getSpaceNum();
	}
	
	private int getLetterAxisMovement(Spaces space, ChessArrayList board) { //FIXME does not need board
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
	
	private void transformPawn(ChessArrayList board) {
		if ((color.equals("B") && currSpace.getSpaceNum() == 1) || (color.equals("W") && currSpace.getSpaceNum() == 8)) {
			boolean selectionComplete = false;
			int choice = -1;
			System.out.println("Which Piece Will the Pawn Tranform Into, 1 (Queen), 2 (Knight), 3 (Rook), 4(Bishop):");
			
			while (selectionComplete == false) {
				if (!scnr.hasNextInt()) {
					scnr.nextLine();
					System.out.println("Invalid Input, Please Choose a Number 1 - 4: ");
				}
				else {
					choice = scnr.nextInt();
					if (choice > 4 || choice < 1) {
						System.out.println("Invalid Input, Please Choose a Number 1 - 4: ");
					}
					else {
						selectionComplete = true;
					}
				}
			}
			
			switch (choice) {
				case 1:		currSpace.setSpace(new Queen(color, board, checkChecker)); break;
				case 2:		currSpace.setSpace(new Knight(pieceNameShortened.substring(1, 2), color, board, checkChecker)); break;
				case 3:		currSpace.setSpace(new Rook(pieceNameShortened.substring(1, 2), color, board, checkChecker)); break;
				case 4:		currSpace.setSpace(new Bishop(pieceNameShortened.substring(1, 2), color, board, checkChecker)); break;
				default:	throw new IllegalArgumentException();
			}
		}
	}
}
