
package Model;

import java.util.ArrayList;

/**
 * @author Bronson Housmans
 *  Holds the observable objects so that they can be
 *         notified when a change occurs !! Determine which objects in the
 *         project we want to implement observable !!
 */
public class Observable {
	ArrayList<Observer> observers = new ArrayList<>();

	/**
	 * Adds an Observer object to the ArrayList of observables
	 * 
	 * @param anObserver - an Observer object
	 */
	public void addObserver(Observer anObserver) {
		observers.add(anObserver);
	}

	/**
	 * Updates the state of the Observable objects
	 * 
	 * @param theObservable - an Observable object - aka a BattleshipGame
	 */
	public void notifyObservers(Observable theObservable) {
		for (Observer obs : observers) {
			obs.update(theObservable);
		}
	}
}
