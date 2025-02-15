package Model;

import javafx.scene.image.ImageView;

/**
 * This class creates an object representing a gunboat with
 * corresponding name and size of 2 
 * @author Cole Hersh
 * @author Rachel Benincasa
 */
public class Gunboat extends Ship {
	/**
	 * Creates a GunBoat object
	 */
	public Gunboat() {
		size = 2;
		name = "Gunboat";
		String imagePath1 = "gunboatH.png"; 
        shipImageH = new ImageView(imagePath1);
        String imagePath2 = "gunboatV.png"; 
        shipImageV = new ImageView(imagePath2);
	}
}
