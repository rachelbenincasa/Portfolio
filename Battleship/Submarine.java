package Model;

import javafx.scene.image.ImageView;

/**
 * This class creates an object representing a submarine with
 * corresponding name and size of 3
 * @author Cole Hersh
 * @author Rachel Benincasa
 */
public class Submarine extends Ship {
	/**
	 * Creates a new Submarine object
	 */
	public Submarine() {
		size = 3;
		name = "Submarine";
		String imagePath1 = "submarineH.png"; 
        shipImageH = new ImageView(imagePath1);
        String imagePath2 = "submarineV.png"; 
        shipImageV = new ImageView(imagePath2);
	}
}
