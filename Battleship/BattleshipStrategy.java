package Model;

/**
 * @author Bronson Housmans
 * This interface is used to define the nextComputerMove method and allow
 * for two strategies to implement it
 */
public interface BattleshipStrategy {
	/**
	 * Method to return the GridPoint object that the computer guesses
	 * @param theGame - a BattleshipGame object
	 * @return a GridPoint object
	 */
	public GridPoint nextComputerMove(Board theGame);
	
	/**
	 * Notifies the strategy that a ship has sunk
	 */
	public void notifyOfSink();
}
