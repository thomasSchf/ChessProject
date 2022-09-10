import java.util.ArrayList;

public class CheckChecker {
	private ChessArrayList board = null;
	private Piece blackKing = null; //maybeUnnecessary
	private Piece whiteKing = null; //same
	private String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
	
	public CheckChecker(ChessArrayList board) {
		this.board = board;
	}
	
	public void setKings(Piece blackKing, Piece whiteKing) {
		this.blackKing = blackKing;
		this.whiteKing = whiteKing;
	}
	
	public boolean checkCheck(String color) { //maybe needs kings color
		//Finds a king's location
		//Searches all possible threats (diagonals, straights,Ls)
		//Stores those as pieces
		// if diagonals fit a role etc then return true
		//If nothing poses a threat 
		
		if (color.equals("B")) {
			return checkCheck(blackKing.getPieceSpace(), "B");
		}
		else {
			return checkCheck(whiteKing.getPieceSpace(), "W");
		}
	}
	
	public boolean checkCheck(Spaces focusSpace, String color) {
		ArrayList<Piece> threats = findThreats(focusSpace, color);
		
		if (threats.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean checkCheckMate(String color) {
		//Find which king to check using color
		//Check all valid move spots, then check if those spots are also in check
		//Check if a piece can neutralize the threat, not possible if threats > 1
		//Check if a piece can intercept the threat (not possible with pawns and knight), also distance needs to be > 1
		Spaces kingFocusSpace = null;
		ArrayList<Piece> threats = null;
		
		if (color.equals("B")) {
			kingFocusSpace = blackKing.getPieceSpace();
		}
		else {
			kingFocusSpace = whiteKing.getPieceSpace();
		}
		
		threats = findThreats(kingFocusSpace, color);
		
		if (threats.size() == 0) {
			return false; //does not account for stalemates, no legal moves that dont result in check but not in check
		}
		else if (checkMoveToSafety(kingFocusSpace)) {
			return false;
		}
		else if (threats.size() > 1) {
			return true;
		}
		else if (checkCapture(threats.get(0))) { //FIXME point of error!!!
			return false;
		}
		else if (getDistance(threats.get(0), kingFocusSpace) < 2) {
			return true;
		}
		else if (checkInterception(threats.get(0), kingFocusSpace, color)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private ArrayList<Piece> findThreats(Spaces kingLocation, String kingColor) {
		ArrayList<Piece> threats = new ArrayList<Piece>();

		checkDiagonal(kingLocation, kingColor, threats);
		checkStraights(kingLocation, kingColor, threats);
		checkKnights(kingLocation, kingColor, threats);
		
		return threats;
	}
	
	private void checkDiagonal(Spaces kingLocation, String kingColor, ArrayList<Piece> threats) { //think of better variable names
		int letterLocation = letterToIndex(kingLocation.getSpaceChar());
		int numLocation = kingLocation.getSpaceNum();
		Piece newPiece = null;
		
		newPiece = diagonalIterator(kingLocation, kingColor, letterLocation, numLocation, 1, 1);
		if (newPiece != null) {
			threats.add(newPiece);
		}
		
		newPiece = diagonalIterator(kingLocation, kingColor, letterLocation, numLocation, -1, 1);
		if (newPiece != null) {
			threats.add(newPiece);
		}
		
		newPiece = diagonalIterator(kingLocation, kingColor, letterLocation, numLocation, 1, -1);
		if (newPiece != null) {
			threats.add(newPiece);
		}
		
		newPiece = diagonalIterator(kingLocation, kingColor, letterLocation, numLocation, -1, -1);
		if (newPiece != null) {
			threats.add(newPiece);
		}
	}
	
	private Piece diagonalIterator(Spaces kingLocation, String kingColor, int startingPointLetter, int startingPointNum, int numDir, int letterDir) {
		int num = startingPointNum + numDir;
		int letter = startingPointLetter + letterDir;
		
		while ((num > 0 && num < 9) && (letter >= 0 && letter < 8)) {
			Piece threatPiece = board.findSpaces(letters[letter], num).getPieceOccupying();
			
			if (threatPiece != null) {
				if (threatPiece.getPieceColor().equals(kingColor)) {
					return null;
				}
				else if (checkMoveViability(threatPiece, kingLocation)) {
					return threatPiece;
				}
				else {
					return null;
				}
			}
			
			num = num + numDir;
			letter = letter + letterDir;
		}
		
		return null;
	}
	
	private void checkStraights(Spaces kingLocation, String kingColor, ArrayList<Piece> threats) {
		int letterLocation = letterToIndex(kingLocation.getSpaceChar());
		int numLocation = kingLocation.getSpaceNum();
		
		Piece newPiece = null;
		
		newPiece = diagonalIterator(kingLocation, kingColor, letterLocation, numLocation, 1, 0);
		if (newPiece != null) {
			threats.add(newPiece);
		}
		
		newPiece = diagonalIterator(kingLocation, kingColor, letterLocation, numLocation, -1, 0);
		if (newPiece != null) {
			threats.add(newPiece);
		}
		
		newPiece = diagonalIterator(kingLocation, kingColor, letterLocation, numLocation, 0, 1);
		if (newPiece != null) {
			threats.add(newPiece);
		}
		
		newPiece = diagonalIterator(kingLocation, kingColor, letterLocation, numLocation, 0, -1);
		if (newPiece != null) {
			threats.add(newPiece);
		}
	}
	
	private void checkKnights(Spaces kingLocation, String kingColor, ArrayList<Piece> threats) {
		int maxMovement = 2;
		int minMovement = 1;
		int letterLocation = letterToIndex(kingLocation.getSpaceChar());
		int numLocation = kingLocation.getSpaceNum();
		
		if (letterLocation + maxMovement < 8 && numLocation + minMovement < 9) {
			String letterNew = letters[letterLocation + maxMovement];
			int numNew = numLocation + minMovement;
			Piece threatPiece = board.findSpaces(letterNew, numNew).getPieceOccupying();
			
			if (checkMoveViability(threatPiece, kingLocation) && !threatPiece.getPieceColor().equals(kingColor)) {
				threats.add(threatPiece);
			}
		}
		if (letterLocation - maxMovement >= 0 && numLocation + minMovement < 9) {
			String letterNew = letters[letterLocation - maxMovement];
			int numNew = numLocation + minMovement;
			Piece threatPiece = board.findSpaces(letterNew, numNew).getPieceOccupying();
			
			if (checkMoveViability(threatPiece, kingLocation) && !threatPiece.getPieceColor().equals(kingColor)) {
				threats.add(threatPiece);
			}
		}
		if (letterLocation + maxMovement < 8 && numLocation - minMovement > 0) {
			String letterNew = letters[letterLocation + maxMovement];
			int numNew = numLocation - minMovement;
			Piece threatPiece = board.findSpaces(letterNew, numNew).getPieceOccupying();
			
			if (checkMoveViability(threatPiece, kingLocation) && !threatPiece.getPieceColor().equals(kingColor)) {
				threats.add(threatPiece);
			}
		}
		if (letterLocation - maxMovement >= 0 && numLocation - minMovement > 0) {
			String letterNew = letters[letterLocation - maxMovement];
			int numNew = numLocation - minMovement;
			Piece threatPiece = board.findSpaces(letterNew, numNew).getPieceOccupying();
			
			if (checkMoveViability(threatPiece, kingLocation) && !threatPiece.getPieceColor().equals(kingColor)) {
				threats.add(threatPiece);
			}
		}
		if (letterLocation + minMovement < 8 && numLocation + maxMovement < 9) {
			String letterNew = letters[letterLocation + minMovement];
			int numNew = numLocation + maxMovement;
			Piece threatPiece = board.findSpaces(letterNew, numNew).getPieceOccupying();
			
			if (checkMoveViability(threatPiece, kingLocation) && !threatPiece.getPieceColor().equals(kingColor)) {
				threats.add(threatPiece);
			}
		}
		if (letterLocation - minMovement >= 0 && numLocation + maxMovement < 9) {
			String letterNew = letters[letterLocation - minMovement];
			int numNew = numLocation + maxMovement;
			Piece threatPiece = board.findSpaces(letterNew, numNew).getPieceOccupying();
			
			if (checkMoveViability(threatPiece, kingLocation) && !threatPiece.getPieceColor().equals(kingColor)) {
				threats.add(threatPiece);
			}
		}
		if (letterLocation + minMovement < 8 && numLocation - maxMovement > 0) {
			String letterNew = letters[letterLocation + minMovement];
			int numNew = numLocation - maxMovement;
			Piece threatPiece = board.findSpaces(letterNew, numNew).getPieceOccupying();
			
			if (checkMoveViability(threatPiece, kingLocation) && !threatPiece.getPieceColor().equals(kingColor)) {
				threats.add(threatPiece);
			}
		}
		if (letterLocation - minMovement >= 0 && numLocation - maxMovement > 0) {
			String letterNew = letters[letterLocation - minMovement];
			int numNew = numLocation - maxMovement;
			Piece threatPiece = board.findSpaces(letterNew, numNew).getPieceOccupying();
			
			if (checkMoveViability(threatPiece, kingLocation) && !threatPiece.getPieceColor().equals(kingColor)) {
				threats.add(threatPiece);
			}
		}
	}
	
	private boolean checkMoveToSafety(Spaces kingLocation) {
		Piece king = kingLocation.getPieceOccupying();
		int letterAsIndex = letterToIndex(kingLocation.getSpaceChar()); //need to test the move for check too
		int numSpot = kingLocation.getSpaceNum();
		boolean canEscape = false;
		
		int newLetterIndex = letterAsIndex + 1;
		int newNumSpot = numSpot + 1;
		canEscape = shipKingEscapeToChecker(newLetterIndex, newNumSpot, king, kingLocation);
		if (canEscape) {
			return true;
		}
		
		newLetterIndex = letterAsIndex + 1;
		newNumSpot = numSpot;
		canEscape = shipKingEscapeToChecker(newLetterIndex, newNumSpot, king, kingLocation);
		if (canEscape) {
			return true;
		}
		
		newLetterIndex = letterAsIndex + 1;
		newNumSpot = numSpot - 1;
		canEscape = shipKingEscapeToChecker(newLetterIndex, newNumSpot, king, kingLocation);
		if (canEscape) {
			return true;
		}
		
		newLetterIndex = letterAsIndex;
		newNumSpot = numSpot + 1;
		canEscape = shipKingEscapeToChecker(newLetterIndex, newNumSpot, king, kingLocation);
		if (canEscape) {
			return true;
		}
		
		newLetterIndex = letterAsIndex;
		newNumSpot = numSpot - 1;
		canEscape = shipKingEscapeToChecker(newLetterIndex, newNumSpot, king, kingLocation);
		if (canEscape) {
			return true;
		}
		
		newLetterIndex = letterAsIndex - 1;
		newNumSpot = numSpot + 1;
		canEscape = shipKingEscapeToChecker(newLetterIndex, newNumSpot, king, kingLocation);
		if (canEscape) {
			return true;
		}
		
		newLetterIndex = letterAsIndex - 1;
		newNumSpot = numSpot;
		canEscape = shipKingEscapeToChecker(newLetterIndex, newNumSpot, king, kingLocation);
		if (canEscape) {
			return true;
		}
		
		newLetterIndex = letterAsIndex - 1;
		newNumSpot = numSpot - 1;
		canEscape = shipKingEscapeToChecker(newLetterIndex, newNumSpot, king, kingLocation);
		if (canEscape) {
			return true;
		}
		
		return canEscape;
	}
	
	private boolean shipKingEscapeToChecker(int newLetterIndex, int newNumSpot, Piece king, Spaces kingLocation) {
		if ((0 <= newLetterIndex && newLetterIndex < 8) && (0 < newNumSpot && newNumSpot < 9)) {
			Spaces newSpace = board.findSpaces(letters[newLetterIndex], newNumSpot);
			boolean canEscape = false;
			
			if (king.takeSpace(newSpace)) {
				Piece tempSpacePiece = newSpace.getPieceOccupying();
				Spaces tempCurrSpace = kingLocation;
				
				if (tempSpacePiece != null && tempSpacePiece.getPieceColor().equals(king.getPieceColor())) {
					return false; //may be redundant
				}
				
				newSpace.setSpace(kingLocation.getPieceOccupying());
				kingLocation.setSpace(null);
				kingLocation = newSpace;
				king.setCurrSpace(newSpace);

				if (!checkCheck(kingLocation, king.getPieceColor())) { //FIXME may be the source of the error
					canEscape = true;
				}
				
				king.setCurrSpace(tempCurrSpace);
				kingLocation = tempCurrSpace;
				kingLocation.setSpace(newSpace.getPieceOccupying());
				newSpace.setSpace(tempSpacePiece);
				return canEscape;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	private boolean checkMoveViability(Piece piece, Spaces threatenedSpace) {
		if (piece == null) {return false;}
		return piece.checkSpaces(threatenedSpace, board) && piece.takeSpace(threatenedSpace);
	}
	
	private boolean checkInterception(Piece threat, Spaces kingLocation, String kingColor) {
		if (threat.getPieceName().equals("Knight") || threat.getPieceName().equals("Pawn")) {return false;}
		
		String color = "";
		int startNum = kingLocation.getSpaceNum();
		int startLetter = letterToIndex(kingLocation.getSpaceChar());
		int endNum = threat.getPieceSpace().getSpaceNum();
		int endLetter = letterToIndex(threat.getPieceSpace().getSpaceChar());
		int letterDirection = (endLetter - startLetter) / Math.abs(endLetter - startLetter);
		int numDirection = (endNum - startNum) / Math.abs(endNum - startNum);
		int pawnDirection = 0;
		
		if (kingColor.equals("B")) {
			color = "W";
			pawnDirection = 1;
		}
		else {
			color = "B";
			pawnDirection = -1;
		}
		
		startNum += numDirection;
		letterDirection += letterDirection;
		
		while ((startNum > 0 && startNum < 9) && (startLetter >= 0 && startLetter < 8)) {
			Spaces goalSpace = board.findSpaces(letters[startLetter], startNum);
			Piece pawnInterceptor = board.findSpaces(letters[startLetter], startNum + pawnDirection).getPieceOccupying();
			
			if (checkCheck(goalSpace, color)) {
				return true;
			}
			else if (pawnInterceptor != null && pawnInterceptor.getPieceName().equals("Pawn") && pawnInterceptor.getPieceColor().equals(kingColor)) {
				if (pawnInterceptor.checkSpaces(goalSpace, board)) {
					return true;
				}
			}
			else if (startNum == 4) {
				Spaces maybePawnSpace = board.findSpaces(letters[startLetter], startNum - 2);
				Piece maybePawn = maybePawnSpace.getPieceOccupying();
				
				if (maybePawn != null && maybePawn.getPieceName().equals("Pawn")) {
					if (maybePawn.checkSpaces(goalSpace, board)) {
						return true;
					}
				}
			}
			else if (startNum == 5) {
				Spaces maybePawnSpace = board.findSpaces(letters[startLetter], startNum + 2);
				Piece maybePawn = maybePawnSpace.getPieceOccupying();
				
				if (maybePawn != null && maybePawn.getPieceName().equals("Pawn")) {
					if (maybePawn.checkSpaces(goalSpace, board)) {
						return true;
					}
				}
			}
			
			startNum = startNum + numDirection;
			startLetter = startLetter + letterDirection;
		}
		
		return false;
	}
	
	private boolean checkCapture(Piece threat) {
		ArrayList<Piece> tThreats = findThreats(threat.getPieceSpace(), threat.getPieceColor());
		
		if (tThreats.size() == 0) {
			return false;
		}
		else {
			for (Piece i : tThreats) {
				Spaces threatSpace = threat.getPieceSpace();
				Piece tThreat = i;
				Spaces tThreatSpace = tThreat.getPieceSpace();
				
				threatSpace.setSpace(tThreat);
				tThreatSpace.setSpace(null);
				tThreat.setCurrSpace(threatSpace);
				boolean causesCheck = checkCheck(tThreat.getPieceColor());
				
				tThreat.setCurrSpace(tThreatSpace);
				tThreatSpace.setSpace(tThreat);
				threatSpace.setSpace(threat);
				
				if (!causesCheck) {
					return true;
				}
			}
			
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
	
	private int getDistance(Piece piece, Spaces kingSpace) {
		int letterLocation = letterToIndex(kingSpace.getSpaceChar());
		int numLocation = kingSpace.getSpaceNum();
		int targetLetterLocation = letterToIndex(piece.getPieceSpace().getSpaceChar());
		int targetNumLocation = piece.getPieceSpace().getSpaceNum();
		
		int differenceLetter = Math.abs(targetLetterLocation - letterLocation);
		int differenceNum = Math.abs(targetNumLocation - numLocation);
		
		if (differenceLetter > differenceNum) {
			return differenceLetter;
		}
		else {
			return differenceNum;
		}
	}
}
