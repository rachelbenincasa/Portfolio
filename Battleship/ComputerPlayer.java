

package Model;
/**
 * @author Bronson Housmans
 * @author Cole Hersh
 * This class creates the computer player and allows for the user to 
 * select which strategy they want to play with. The default is the randomAI.
 * Will return a GridPoint of the desired move by the computer player to the game.
 */
public class ComputerPlayer {

	private BattleshipStrategy currStrat;

	/**
	 * Constructor for the computer player. Sets the current strategy for the game
	 * to the random AI
	 */
	public ComputerPlayer() {
		currStrat = new RandomAI();
	}

	/**
	 * Takes a BattleshipStrategy to change the AI that is being used to play the 
	 * game. Changes the instance variable for the class
	 * @param inputStrategy - an object that implements the BattleshipStrategy interface
	 */
	public void setDifficulty(BattleshipStrategy inputStrategy) {
		currStrat = inputStrategy;
	}

	/**
	 * Guesses a spot on the user's board 
	 * @param theGame - a BattleshipGame object
	 * @return GridPoint of the spot on the grid that the computer decides to guess
	 */
	public GridPoint guessSpot(Board theGame) {
		return currStrat.nextComputerMove(theGame);
	}
	
	/**
	 * Notifies the current strategy that a ship has sunk
	 * Note: the RandomAI ignores this
	 */
	public void notifySink() {
		currStrat.notifyOfSink();
	}
}
