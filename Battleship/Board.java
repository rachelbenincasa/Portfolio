package Model;

import javafx.scene.control.Button;
//
/** 
 * This class holds the 8x8 grid of GridPoint objects
 * @author Cole Hersh
 * @author Emma Elliot
 */
public class Board {
	
	// initializes an 8x8 2d array of button gridPoints
	private GridPoint[][] board;
	
	/**
	 * Takes boolean parameter to represent if the board belongs
	 * to the user or the computer. Creates a 2D array of gridPoints
	 * @param isOpponen - a boolean that is true if this is the opponents board
	 * TODO - deprecate isOpponen
	 */
	public Board(boolean isOpponen) {
		 board = new GridPoint[8][8];
		 for (int i = 0; i < 8; i++) {
			 for (int j = 0; j < 8; j++) {
				 board[i][j] = new GridPoint(isOpponen);
				 board[i][j].setCol(j);
				 board[i][j].setRow(i);
			 }
		 }
		 //isOpponent = isOpponen;
	}
	
	/**
	 * Return the 2D array of gridPoint objects
	 * @return a 2D array of GridPoints
	 */
	public GridPoint[][] getBoard() {
		return board;
	}
	
	/**
	 * Return the Button from the GridPoint class at a specified index in the 2D array
	 * @param i - an int that is the row in the array
	 * @param j - an int that is the column in the array
	 * @return a Button object
	 */
	public Button getPoint(int i, int j) {
		return board[i][j].getButton();
	}
	
	/**
	 * return the GridPoint at an index in the 2D array
	* @param i - an int that is the row in the array
	 * @param j - an int that is the column in the array
	 * @return a GridPoint object
	 */
	public GridPoint getGridPoint(int i, int j) {
		return board[i][j].getGP();
	}

}
