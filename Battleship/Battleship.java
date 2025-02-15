package Model;

import javafx.scene.image.ImageView;

/**
 * This class creates an object representing a battleship with
 * corresponding name and size of 4
 * @author Cole Hersh
 * @author Rachel Benincasa
 */
public class Battleship extends Ship {
	/**
	 * Creates a new Battleship object
	 */
	public Battleship() {
		size = 4;
		name = "Battleship";
		String imagePath1 = "battleshipH.png"; 
        shipImageH = new ImageView(imagePath1);
        String imagePath2 = "battleshipV.png"; 
        shipImageV = new ImageView(imagePath2);
	}
}
