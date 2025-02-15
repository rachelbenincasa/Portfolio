package Model;

import javafx.geometry.Insets;
import javafx.scene.control.Button;

/**
 * @author Emma Elliot
 * @author Cole Hersh
 * This class holds the button and the ship that makes up each gridpoint on the board
 */
public class GridPoint {

	private Button button;
	private boolean isHit;
	private String shipName;
	private Ship ship;
	//private boolean isOpp;
	private int row;
	private int col;

	/**
	 * Takes boolean parameter to determine if point belongs to user or computer
	 * Uses button for the display in the GUI and instance variables to
	 * represent being guessed and the ship name that is placed on the point
	 * @param opp
	 * Note: opp is never used - TODO deprecate it
	 */
	public GridPoint(boolean opp) {
		//isOpp = opp;
		isHit = false;
		shipName = "Empty";
		row = 0;
		col = 0;
		button = new Button();
		button.setPrefSize(40, 40);
		ship = null;
		button.getStyleClass().add("gridpointDefault");

	}
	
	public void resize() {
		button.setPrefSize(30, 30);
	}

	/**
	 * Returns the button instance variable
	 * @return a Button object
	 */
	public Button getButton() {
		return button;
	}

	/**
	 * Returns the GridPoint object
	 * @return this GridPoint object
	 */
	public GridPoint getGP() {
		return this;
	}

	/**
	 * Sets the isHit variable to true
	 * @param guessVal - a boolean that is true if the GridPoint is hit
	 */
	public void setGuess(boolean guessVal) {
		isHit = guessVal;
	}

	/**
	 * Returns if the point has been guessed
	 * @return a boolean
	 */
	public boolean guessed() {
		return isHit;
	}

	/**
	 * Sets the name of the ship located on the point
	 * @param name - a String that is the ship's name
	 */
	public void setShipName(String name) {
		shipName = name;
	}
	
	/**
	 * Sets the ship object and the associated name
	 * @param setShip - a Ship object
	 */
	public void setShip(Ship setShip) {
		ship = setShip;
		if(ship == null) {
			shipName = "Empty";
		}
		else {
			shipName = ship.name();
		}
	}

	/**
	 * Returns the name of the ship in the point
	 * @return a string
	 */
	public String getShipName() {
		return shipName;
	}
	
	/**
	 * Gets the GirdPoint's ship
	 * @return a Ship object
	 */
	public Ship getShip() {
		return ship;
	}
	
	/**
	 * Set the value of the row that the GridPoint is in
	 * @param rowIn - an int between 0 and 7
	 */
	public void setRow(int rowIn) {
		row = rowIn;
	}
	
	/**
	 * Return the row of the GridPoint
	 * @return an int
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Set the column the GridPoint is in
	 * @param colIn - an int between 0 and 7
	 */
	public void setCol(int colIn) {
		col = colIn;
	}
	
	/**
	 * Return the column the GridPoint is in
	 * @return an int
	 */
	public int getCol() {
		return col;
	}

}
