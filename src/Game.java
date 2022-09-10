import java.util.Scanner;

public class Game {
	private String player1Name = "";
	private String player2Name = "";
	private String gameName = "";
	private boolean gameFinished = false;
	private GenBoard gameBoard = new GenBoard();
	private Scanner scnr = new Scanner(System.in);
	
	public Game(String player1Name, String player2Name, String gameName) {
		this.player1Name = player1Name;
		this.player2Name = player2Name;
		this.gameName = gameName;
	}
	
	/**
	 * Begins the actual game and runs through its processes until
	 * the game is finished.
	 */
	public String PlayGame() {
		int winningPlayerNum = 0;
		
		System.out.println("Playing " + gameName);
		
		while (gameFinished == false) { //FIXME remove this while, the count variable
			gameBoard.printBoard();
			String player1Input = getPlayerInput(1);
			while (!gameBoard.doMove(player1Input, 1)) {
				System.out.println("This move is impossible.");
				player1Input = getPlayerInput(1);
			}
			
			if (gameBoard.checkIfWinner()) {
				gameFinished = true;
				break;
			}
			
			gameBoard.printBoard();
			String player2Input = getPlayerInput(2);
			while (!gameBoard.doMove(player2Input, 2)) {
				System.out.println("This move is impossible.");
				player2Input = getPlayerInput(2);
			}
			gameFinished = gameBoard.checkIfWinner();
		}
		
		winningPlayerNum = gameBoard.getWinner();
		if (winningPlayerNum == 1) {
			return player1Name;
		}
		else if (winningPlayerNum == 2) {
			return player2Name;
		}
		else {
			throw new IllegalArgumentException("Winning Player Number Error");
		}
		
	}
	
	private String getPlayerInput(int playerNum) {
		String playerName = "";
		String playerChoice = "";
		String tempPlayerChoice = "";
		
		if (playerNum == 1) {
			playerName = player1Name;
		}
		else {
			playerName = player2Name;
		}
		
		System.out.println(playerName + ", choose the space with the piece you wish to move");
		playerChoice = scnr.next() + " ";
		
		while (!checkIfLetterIsWithinBounds(playerChoice.substring(0, 1)) || playerChoice.length() > 3 || !checkIfNumIsWithinBounds(playerChoice.substring(1,2))) {
			System.out.println("Invalid choice, Please Try Again (note: do lowercase letter then number as 2 back to back characters):");
			playerChoice = scnr.next() + " ";
		}
		System.out.println(playerName + ", choose where you want it to move");
		tempPlayerChoice = scnr.next();
		
		while (!checkIfLetterIsWithinBounds(tempPlayerChoice.substring(0, 1)) || tempPlayerChoice.length() > 3 || !checkIfNumIsWithinBounds(tempPlayerChoice.substring(1,2))) {
			System.out.println("Invalid choice, Please Try Again (note: do lowercase letter then number as 2 back to back characters):");
			tempPlayerChoice = scnr.next();
		}
		
		playerChoice = playerChoice + tempPlayerChoice;
		return playerChoice;
	}
	
	private boolean checkIfLetterIsWithinBounds(String letter) {
		String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
		
		for (String i : letters) {
			if (i.equals(letter)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean checkIfNumIsWithinBounds(String num) {
		try {
			int number = Integer.parseInt(num);
			
			if (number > 8 || number < 1) {
				return false;
			}
			
			return true;
		}
		catch (NumberFormatException exeption) {
			return false;
		}
	}
}
