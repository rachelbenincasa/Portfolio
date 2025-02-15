package Model;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Bronson Housmans
 * @author Cole Hersh This class is the hardAI strategy logic for the game
 */
public class HardAI implements BattleshipStrategy {

	private Random generator;
	private boolean trackingShip;
	private boolean onLine;
	private ArrayList<GridPoint> nextGuesses;
	private GridPoint nextLinePoint;
	private GridPoint baseGuess;
	private Direction currDirection;

	enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	/**
	 * Creates a new HardAI object
	 */
	public HardAI() {
		generator = new Random();
		trackingShip = false;
		onLine = false;
		nextGuesses = new ArrayList<GridPoint>();
		baseGuess = new GridPoint(true);
		nextLinePoint = new GridPoint(true);
		currDirection = Direction.UP;
	}

	@Override
	/**
	 * This class gets the CPU move
	 * 
	 * @param theGame - a BattleshipGame object
	 */
	public GridPoint nextComputerMove(Board theGame) {
		// based on the previous move, make a guess
		if (!trackingShip) {
			return randomGuess(theGame);
		}
		return aimedGuess(theGame);
	}

	private GridPoint randomGuess(Board theGame) {
		// returns a GridPoint value for the position of the next computer move
		boolean notAvailable = true;
		while (notAvailable) {
			// Otherwise, try to randomly find an open spot
			int row = generator.nextInt(8);
			int col = generator.nextInt(8);
			GridPoint currGridPoint = theGame.getGridPoint(row, col);
			if (!currGridPoint.guessed()) {
				notAvailable = false;
				if (!currGridPoint.getShipName().equals("Empty")) {
					baseGuess = currGridPoint;
					trackingShip = true;
					createNextMoveOptions(row, col, theGame);
				}
				return currGridPoint;
			}
		}
		return null; // Avoid a compile-time error
	}

	private GridPoint aimedGuess(Board theGame) {
		GridPoint currGP;
		if (onLine) {
			currGP = nextLinePoint;
			if (!nextLinePoint.getShipName().equals("Empty")) {
				setNextLinePoint(nextLinePoint.getRow(), nextLinePoint.getCol(), theGame);
			} else {
				onLine = false;
			}
		} else {
			if (nextGuesses.size() == 0) {
				onLine = false;
				trackingShip = false;
				return randomGuess(theGame);
			} else {
				currGP = nextGuesses.remove(0);
				// determine if there is a ship on the point to be guessed next
				if (!currGP.getShipName().equals("Empty")) {
					int row = currGP.getRow();
					int col = currGP.getCol();
					onLine = true;
					setNextLinePoint(row, col, theGame);
					if (nextGuesses.size() > 0)
						getOtherPoint();
				}
				
			}
			
		}
		return currGP;
	}

	private void setNextLinePoint(int row, int col, Board theGame) {
		// set the direction based on the relation to the original point that resulted
		// in a hit
		if (row > baseGuess.getRow()) {
			currDirection = Direction.DOWN;
			if (row < 7)
				nextLinePoint = theGame.getGridPoint(row + 1, col);
			else
				onLine = false;
		} else if (row < baseGuess.getRow()) {
			currDirection = Direction.UP;
			if (row > 0)
				nextLinePoint = theGame.getGridPoint(row - 1, col);
			else
				onLine = false;
		} else if (col > baseGuess.getCol()) {
			currDirection = Direction.RIGHT;
			if (col < 7)
				nextLinePoint = theGame.getGridPoint(row, col + 1);
			else
				onLine = false;
		} else {
			currDirection = Direction.LEFT;
			if (col > 0)
				nextLinePoint = theGame.getGridPoint(row, col - 1);
			else
				onLine = false;
		}
	}

	private void getOtherPoint() {
		// remove the other points in the ArrayList that do not have equal row/col vals
		switch (currDirection) {
		case UP:
			while (nextGuesses.size() > 1) {
				GridPoint compGP = nextGuesses.remove(0);
				if (compGP.getCol() == baseGuess.getCol()) {
					nextGuesses.add(compGP);
				}
			}
		case DOWN:
			while (nextGuesses.size() > 1) {
				GridPoint compGP = nextGuesses.remove(0);
				if (compGP.getCol() == baseGuess.getCol()) {
					nextGuesses.add(compGP);
				}
			}
		case LEFT:
			while (nextGuesses.size() > 1) {
				GridPoint compGP = nextGuesses.remove(0);
				if (compGP.getRow() == baseGuess.getRow()) {
					nextGuesses.add(compGP);
				}
			}
		case RIGHT:
			while (nextGuesses.size() > 1) {
				GridPoint compGP = nextGuesses.remove(0);
				if (compGP.getRow() == baseGuess.getRow()) {
					nextGuesses.add(compGP);
				}
			}
		}
	}

	private void createNextMoveOptions(int row, int col, Board theGame) {
		GridPoint toAdd;
		// add the point at the row above if possible
		if (row > 0) {
			toAdd = theGame.getGridPoint(row - 1, col);
			if (!toAdd.guessed()) {
				nextGuesses.add(toAdd);
			}
		}
		// add the point at the row below if possible
		if (row < 7) {
			toAdd = theGame.getGridPoint(row + 1, col);
			if (!toAdd.guessed()) {
				nextGuesses.add(toAdd);
			}
		}
		// add the point at the column to the left is possible
		if (col > 0) {
			toAdd = theGame.getGridPoint(row, col - 1);
			if (!toAdd.guessed()) {
				nextGuesses.add(toAdd);
			}
		}
		// add the point at the column to the right if possible
		if (col < 7) {
			toAdd = theGame.getGridPoint(row, col + 1);
			if (!toAdd.guessed()) {
				nextGuesses.add(toAdd);
			}
		}
	}

	@Override
	/**
	 * Notifies the strategy that the ship it is targeting has sunk
	 */
	public void notifyOfSink() {
		// changes the trackingShip variable to false
		trackingShip = false;
		onLine = false;
		nextGuesses.clear();
	}
}
