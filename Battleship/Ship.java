package Model;

import javafx.scene.image.ImageView;

/**
 * This Superclass hold the methods and instance variables of all ships Allows
 * for an array of type Ship to better organize code
 * @author Cole Hersh
 * @author Rachel Benincasa
 */
public abstract class Ship {
	String name;
	int size;
	boolean isSunk;
	int hitTracker;
	ImageView shipImageH;
	ImageView shipImageV;
	// Keeps track of which parts of the ship are hit

	/**
	 * Creates a ship object that is not sunk
	 */
	public Ship() {
		isSunk = false;
		hitTracker = 0;
		shipImageH = null;
		shipImageV = null;
	}

	/**
	 * Returns if the ship is sunk
	 * 
	 * @return a boolean that is true if the ship is sunk and false otherwise
	 */
	public boolean isSunk() {
		return isSunk;
	}

	/**
	 * Returns the size on the ship
	 * 
	 * @return an int that is the ships size in grid points
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns the ship's name
	 * 
	 * @return a String that is the ship name
	 */
	public String name() {
		return name;
	}

	/**
	 * Returns how many hits the ship has taken
	 * 
	 * @return an int that how many times the ship has been hit
	 */
	public int hitTracker() {
		return hitTracker;
	}

	/**
	 * Returns if the ship is sunk or not
	 * 
	 * @return a boolean represting if the ship has sunk
	 */
	public boolean checkSunk() {
		return hitTracker == size;
	}

	/**
	 * Increases the hitTracker
	 */
	public void addHit() {
		hitTracker++;
	}

	/**
	 * Set the hitTracker to 0
	 */
	public void resetHits() {
		hitTracker = 0;
	}

	/**
	 * Gets the ship's horizontal image
	 * 
	 * @return an Image
	 */
	public ImageView getShipImageH() {
		return shipImageH;
	}

	/**
	 * sets the ship's horizontal image
	 * 
	 * @param shipImageH - an Image
	 */
	public void setShipImageH(ImageView shipImageH) {
		this.shipImageH = shipImageH;
	}

	/**
	 * Gets the ship's vertical image
	 * 
	 * @return an Image
	 */
	public ImageView getShipImageV() {
		return shipImageV;
	}

	/**
	 * sets the ship's vertical image
	 * 
	 * @param shipImageV - an Image
	 */
	public void setShipImageV(ImageView shipImageV) {
		this.shipImageV = shipImageV;
	}
}
