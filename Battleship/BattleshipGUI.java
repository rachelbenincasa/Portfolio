package view_controller;

import Model.BattleshipGame;
import Model.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * This class controls the GUI for the entire game by handling the switching
 * from different views. It is also the main class. It extends the JavaFX
 * application class.
 * 
 * @author Cole Hersh
 * @author Rachel Benincasa
 */

public class BattleshipGUI extends Application implements Observer {
	/**
	 * Runs the entire application
	 * 
	 * @param args - None
	 */
	public static void main(String[] args) {
		launch(args);
	}

	private BattleshipGame theGame;

	// set up views
	private Observer currentView;
	private Observer gameView;
	private Observer mainMenuView;
	private Observer shipSelectorView;
	private static Stage stage;
	
	
	private BorderPane window;

	/**
	 * This method initializes the GUI
	 * 
	 * @param theStage - The stage on which the GUI is displayed on
	 */
	@Override
	public void start(Stage theStage) throws Exception {
		stage = theStage;
		stage.setTitle("Battleship");
		window = new BorderPane();
		Scene scene = new Scene(window, 750, 550);
		scene.getStylesheets().add("BattleshipStyle/style.css");
		// initializeGameForTheFirstTime();

		// create and add views
		theGame = new BattleshipGame();
		gameView = new GameView(theGame);
		shipSelectorView = new ShipSelectorView(theGame);
		mainMenuView = new MainMenuView(theGame);
		theGame.addObserver(gameView);
		theGame.addObserver(mainMenuView);
		theGame.addObserver(shipSelectorView);
		theGame.addObserver(this);
		setViewTo(mainMenuView);
		setMenu();
		stage.setScene(scene);
		stage.show();
	}

	private void setViewTo(Observer newView) {
		window.setCenter(null);
		currentView = newView;
		window.setCenter((Node) currentView);
	}

	/**
	 * returns the GUI stage (used for popup in isGameOver)
	 * 
	 * @return the stage
	 */
	public static Stage getStage() {
		return stage;
	}

	private void setMenu() {
		MenuItem restart = new MenuItem("Restart");
		MenuItem quit = new MenuItem("Quit");
//		MenuItem randomAI = new MenuItem("Easy");
//		MenuItem hardAI = new MenuItem("Hard");
//		Menu difficulty = new Menu("Set Difficulty");
//		difficulty.getItems().addAll(randomAI, hardAI);
		Menu options = new Menu("Options");
		options.getItems().addAll(restart, quit);
		MenuBar bar = new MenuBar();
		bar.getMenus().addAll(options);
		window.setTop(bar);

		EventHandler<ActionEvent> itemHandler = new MenuHandler();
		restart.setOnAction(itemHandler);
		quit.setOnAction(itemHandler);
//		randomAI.setOnAction(itemHandler);
//		hardAI.setOnAction(itemHandler);
	}

	// helper class to handle menu events
	private class MenuHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			// determines what action to take based on the user's response
			String text = ((MenuItem) event.getSource()).getText();
			if (text.equals("Restart")) {
				if (theGame.getView() != 0) {
					theGame.setView(4);
					restartGame();
				}
			} else if (text.equals("Quit")) {
				theGame.setView(0);
				restartGame();
			}

		}	

	}
	
	private void restartGame() {
		theGame.restartGame();
		shipSelectorView = new ShipSelectorView(theGame);
		mainMenuView = new MainMenuView(theGame);
		Popup p = ((GameView) gameView).getPopup();
		if (p != null) {
			p.hide();
		}
		gameView = new GameView(theGame);
	}
	
	/**
	 * Will update GUI view depending on users input into the menu
	 * 
	 * @param theObserved - the observers of the BattleShip GUI
	 */

	@Override
	public void update(Object theObserved) {
		if(theGame.getRestart()) {
			theGame.setView(4);
			restartGame();
			
		}
		else {
		switch (theGame.getView()) {
		case 0:
			stage.setWidth(750);
			stage.setHeight(550);
			setViewTo(mainMenuView);
			break;

		case 1:
			stage.setWidth(750);
			stage.setHeight(800);
			setViewTo(shipSelectorView);
			break;
		case 2:
			stage.setWidth(650);
			stage.setHeight(900);
			setViewTo(gameView);
			((GameView) gameView).setUserBoard();
			
			break;
		case 3:
			stage.setWidth(650);
			stage.setHeight(900);
			setViewTo(new GameView(theGame));
			break;
		case 4:
			stage.setWidth(750);
			stage.setHeight(800);
			setViewTo(new ShipSelectorView(theGame));
			break;
		}
		}
		
		
	}

}
