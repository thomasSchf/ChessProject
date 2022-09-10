import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		Game game = null;
		String player1Name = "";
		String player2Name = "";
		String winner = "";
		
		System.out.println("Enter 1st Player Name:");
		player1Name = scnr.nextLine();
		System.out.println("Enter 2nd Player Name:");
		player2Name = scnr.nextLine();
		
		if (player1Name.equals("Phil") && player2Name.equals("Thomas")) {
			game = new Game(player1Name, player2Name, "Checkers");
		}
		else {
			game = new Game(player1Name, player2Name, "Chess");
		}
		
		winner = game.PlayGame();
		System.out.println("Congratulations, " + winner + " wins!");
		
		scnr.close();
	}

}
