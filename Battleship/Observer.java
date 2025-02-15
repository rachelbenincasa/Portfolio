/**
 * @author Bronson Housmans
 * Interface for the observable implementation of this project. Only
 * has method to update the observed object.
 */

package Model;

/**
 * Interface for an update method for Observable objects
 */
public interface Observer {
	/**
	 * updates the observers
	 * @param theObserved - an Observable - aka a BattheshipGame object
	 */
	void update(Object theObserved);
}
