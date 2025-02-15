package Model;

import javafx.scene.image.ImageView;
/**
 * This class stores the starting values of each ship to be able to place images on the board when game is loaded
 * @author Rachel Benincasa
 * @author Cole Hersh
 */
public class ShipPlacement {
    private Ship ship;
    private int startRow;
    private int startCol;
    private boolean isHorizontal;
    
    /**
     * 
     * @param ship - a Ship object
     * @param startRow -  an int that is the row where the ship starts
     * @param startCol - an int the column where the ship starts
     * @param isHorizontal - a boolean that is true if the ship is horizontal
     */
    public ShipPlacement(Ship ship, int startRow, int startCol, boolean isHorizontal) {
        this.ship = ship;
        this.startRow = startRow;
        this.startCol = startCol;
        this.isHorizontal = isHorizontal;
    }
    /**
     * Returns the ship object
     * @return a ship object
     */
    public Ship getShip() {
        return ship;
    }
    
    /**
     * Gets the starting row of the ship
     * @return an int
     */
    public int getStartRow() {
        return startRow;
    }
    
    
    /**
     * Sets the starting row of the ship
     * @param row - an int between 0 and 7
     */
    public void setStartRow(int row) {
       startRow = row;
    }
    
    /**
     * Gets the starting column of the ship
     * @return an int
     */
    public int getStartCol() {
        return startCol;
    }
    
    
    /**
     * Sets the starting column of the ship
     * @param col - an int between 0 and 7
     */
    public void setStartCol(int col) {
       startCol = col;
    }
    
    /**
     * Gets if the ship is horizontal
     * @return a boolean
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }
    
    /**
     * sets the Ship's orientation
     * @param isHoriz - a boolean that is true if the ship is horizontal
     */
    public void  setIsHorizontal(boolean isHoriz) {
    	isHorizontal = isHoriz;
    }
    
    /**
     * Gets the horizontal or vertical image based on the ships orientation
     * @return an Image object
     */
    public ImageView getShipImage() {
    	if (isHorizontal) {
    		return ship.getShipImageH();
    	} else {
    		return ship.getShipImageV();
    	}
    }
}