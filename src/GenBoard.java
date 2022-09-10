
public class GenBoard {
	private int winner = 0;
	private ChessArrayList boardArray = new ChessArrayList();
	private CheckChecker checkChecker = null;
	
	public GenBoard() {
		//Kings
		checkChecker = new CheckChecker(boardArray);
		
		Piece whiteKing = new King("W", boardArray, checkChecker);
		Piece blackKing = new King("B", boardArray, checkChecker);
		
		checkChecker.setKings(blackKing, whiteKing);
		boardArray.findSpaces("e", 1).setSpace(whiteKing);
		boardArray.findSpaces("e", 8).setSpace(blackKing);
		
		//White pawns
		boardArray.findSpaces("a", 2).setSpace(new Pawn("a", "W", boardArray, checkChecker));
		boardArray.findSpaces("b", 2).setSpace(new Pawn("b", "W", boardArray, checkChecker));
		boardArray.findSpaces("c", 2).setSpace(new Pawn("c", "W", boardArray, checkChecker));
		boardArray.findSpaces("d", 2).setSpace(new Pawn("d", "W", boardArray, checkChecker));
		boardArray.findSpaces("e", 2).setSpace(new Pawn("e", "W", boardArray, checkChecker));
		boardArray.findSpaces("f", 2).setSpace(new Pawn("f", "W", boardArray, checkChecker));
		boardArray.findSpaces("g", 2).setSpace(new Pawn("g", "W", boardArray, checkChecker));
		boardArray.findSpaces("h", 2).setSpace(new Pawn("h", "W", boardArray, checkChecker));
		
		//Black pawns
		boardArray.findSpaces("a", 7).setSpace(new Pawn("a", "B", boardArray, checkChecker));
		boardArray.findSpaces("b", 7).setSpace(new Pawn("b", "B", boardArray, checkChecker));
		boardArray.findSpaces("c", 7).setSpace(new Pawn("c", "B", boardArray, checkChecker));
		boardArray.findSpaces("d", 7).setSpace(new Pawn("d", "B", boardArray, checkChecker));
		boardArray.findSpaces("e", 7).setSpace(new Pawn("e", "B", boardArray, checkChecker));
		boardArray.findSpaces("f", 7).setSpace(new Pawn("f", "B", boardArray, checkChecker));
		boardArray.findSpaces("g", 7).setSpace(new Pawn("g", "B", boardArray, checkChecker));
		boardArray.findSpaces("h", 7).setSpace(new Pawn("h", "B", boardArray, checkChecker));
		
		//Queens
		boardArray.findSpaces("d", 1).setSpace(new Queen("W", boardArray, checkChecker)); 
		boardArray.findSpaces("d", 8).setSpace(new Queen("B", boardArray, checkChecker));
		
		//Knights
		boardArray.findSpaces("b", 1).setSpace(new Knight("b", "W", boardArray, checkChecker));
		boardArray.findSpaces("b", 8).setSpace(new Knight("b", "B", boardArray, checkChecker));
		boardArray.findSpaces("g", 1).setSpace(new Knight("g", "W", boardArray, checkChecker));
		boardArray.findSpaces("g", 8).setSpace(new Knight("g", "B", boardArray, checkChecker));
		
		//Bishops
		boardArray.findSpaces("c", 1).setSpace(new Bishop("c", "W", boardArray, checkChecker));
		boardArray.findSpaces("c", 8).setSpace(new Bishop("c", "B", boardArray, checkChecker));
		boardArray.findSpaces("f", 1).setSpace(new Bishop("f", "W", boardArray, checkChecker));
		boardArray.findSpaces("f", 8).setSpace(new Bishop("f", "B", boardArray, checkChecker)); 
		
		//Rooks
		boardArray.findSpaces("a", 1).setSpace(new Rook("a", "W", boardArray, checkChecker));
		boardArray.findSpaces("a", 8).setSpace(new Rook("a", "B", boardArray, checkChecker));
		boardArray.findSpaces("h", 1).setSpace(new Rook("h", "W", boardArray, checkChecker));
		boardArray.findSpaces("h", 8).setSpace(new Rook("h", "B", boardArray, checkChecker));
	}
	
	public int getWinner() { //FIXME
		return winner;
	}
	
	public void printBoard() { 
		System.out.println("");
		System.out.println("");
		System.out.print("   | a | b | c | d | e | f | g | h |");
		System.out.print("\n---------------------------------------\n");
		System.out.print(" 8 |");
		
		for (int i = 8; i > 0; --i) {
			Spaces currSpace = null;
			String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
			
			for (int j = 0; j < 8; ++j) {
				currSpace = boardArray.findSpaces(letters[j], i);
				
				if (currSpace.getPieceOccupying() == null) {
					System.out.print("   |");
				}
				else {
					System.out.print(currSpace.getPieceOccupying().getPieceNameShortened() + "|");
				}
			}
			
			if (i - 1 != 0 ) {
				System.out.println(" " + (i));
				System.out.print("---------------------------------------\n " + (i - 1) + " |");
			}
			else {
				System.out.print(" " + (i));
			}
		}
		System.out.print("\n---------------------------------------\n");
		System.out.print("   | a | b | c | d | e | f | g | h |");
		System.out.println("");
		System.out.println("");
		System.out.println("");
	}
	
	public boolean checkIfWinner() { //FIXME
		if (checkChecker.checkCheckMate("B")) {
			winner = 1;
			return true;
		}
		else if (checkChecker.checkCheckMate("W")) {
			winner = 2;
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean doMove(String moves, int playerNum) {
		if(moves.length() > 5) {return false;}
		
		String firstLetterPos = getLetterPos(moves.substring(0, 2));
		int firstNumPos = getNumPos(moves.substring(0, 2));
		String secondLetterPos = getLetterPos(moves.substring(3, 5));
		int secondNumPos = getNumPos(moves.substring(3, 5));
		
		Piece piece = boardArray.findSpaces(firstLetterPos, firstNumPos).getPieceOccupying();
		if (piece == null) {
			return false;
		}
		else if (playerNum == 1 && piece.getPieceColor().equals("B")) {
			return false;
		}
		else if (playerNum == 2 && piece.getPieceColor().equals("W")) {
			return false;
		}
		
		Spaces goalSpace = boardArray.findSpaces(secondLetterPos, secondNumPos);
		
		return piece.move(goalSpace, boardArray);
	}
	
	private int getNumPos(String move) {
		return Integer.parseInt(move.substring(1, 2));
	}
	
	private String getLetterPos(String move) {
		return move.substring(0, 1);
	}
}
