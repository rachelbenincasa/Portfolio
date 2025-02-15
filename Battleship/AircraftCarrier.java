package Model;


import javafx.scene.image.ImageView;

/**
 * This class creates an object representing an aircraft carrier with
 * corresponding name and size of 5
 * @author Cole Hersh
 * @author Rachel Benincasa
 */
public class AircraftCarrier extends Ship {
	/**
	 * Creates a new AircraftCarrier object
	 */
	public AircraftCarrier() {
		size = 5;
		name = "Aircraft Carrier";
		String imagePath1 = "aircraftcarrierH.png"; 
        shipImageH = new ImageView(imagePath1);
        String imagePath2 = "aircraftcarrierV.png"; 
        shipImageV = new ImageView(imagePath2);
	}
}
