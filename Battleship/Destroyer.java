package Model;

import javafx.scene.image.ImageView;

/**
 * This class creates an object representing a destroyer with
 * corresponding name and size of 3
 * @author Cole Hersh
 * @author Rachel Benincasa
 */
public class Destroyer extends Ship{
	/**
	 * Creates a new Destroyer object
	 */
	public Destroyer() {
		size = 3;
		name = "Destroyer";
		String imagePath1 = "destroyerH.png"; 
        shipImageH = new ImageView(imagePath1);
        String imagePath2 = "destroyerV.png"; 
        shipImageV = new ImageView(imagePath2);
	}
}
