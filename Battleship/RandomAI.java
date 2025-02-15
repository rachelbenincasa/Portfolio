
package Model;

import java.util.Random;

/**
 * @author Bronson Housmans 
 * @author Cole Hersh
 * This class is for the random AI. It will return a
 *         GridPoint for where the next attempted move will be from the computer
 */

public class RandomAI implements BattleshipStrategy {

	private static Random generator;

	/**
	 * Initializes the RandomAI. Makes a new Random object
	 */
	public RandomAI() {
		generator = new Random();
	}

	@Override
	/**
	 * Return a GridPoint for the next available spot on the board that the computer
	 * decides to guess
	 */
	public GridPoint nextComputerMove(Board theGame) {
		// returns a gridPoint value for the position of the next computer move
		boolean notAvailable = true;
		while (notAvailable) {
			// if (theGame.maxMovesRemaining() == 0)
			// throw new Exception(" -- Hey there programmer, the board is filled");

			// Otherwise, try to randomly find an open spot
			int row = generator.nextInt(8);
			int col = generator.nextInt(8);
			GridPoint currGridPoint = theGame.getGridPoint(row, col);
			if (!currGridPoint.guessed()) {
				notAvailable = false;
				return currGridPoint;
			}
		}
		return null; // Avoid a compile-time error
	}

	@Override
	/**
	 * Notifies the AI that a ship is sunk Note: required as is an interface method
	 * Has no implementation in RandomAI class
	 */
	public void notifyOfSink() {
		// not needed for the randomAI
	}
}
