package view_controller;

import java.util.ArrayList;

import Model.AircraftCarrier;
import Model.Battleship;
import Model.BattleshipGame;
import Model.Board;
import Model.Destroyer;
import Model.GridPoint;
import Model.Gunboat;
import Model.Observer;
import Model.Ship;
import Model.ShipPlacement;
import Model.Submarine;
import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.util.Duration;
/**
 * This class creates the ship selector, where the user will set up the location
 * and orientation of each ship
 * 
 * @author Cole Hersh
 * @author Rachel Benincasa
 * @author Emma Elliott
 */
public class ShipSelectorView extends BorderPane implements Observer {
	private BattleshipGame theGame;
	private Board shipBoard;
	private GridPane shipGrid;
	private RadioButton sub;
	private RadioButton destroyer;
	private RadioButton battleship;
	private RadioButton carrier;
	private RadioButton gunboat;
	private RadioButton vert;
	private RadioButton horiz;
	private ToggleGroup shipType;
	private ToggleGroup orientation;
	private ArrayList<Ship> ships;
	private ArrayList<Ship> shipsLeft;
	private GridPane radioButtons;
	private Label info;
	private Ship shipSelected;
	private int orientationSelected;
	private Button startGame;
	private Label errormsg = new Label("Ship cannot be placed there! Please retry.");
	private boolean restarted = false;

	
	/**
	 * Creates a new  ShipSelectorView object
	 * @param theModel - the game logic
	 */
	@SuppressWarnings("unchecked")
	public ShipSelectorView(BattleshipGame theModel) {
		orientationSelected = 0;
		theGame = theModel;
		// adds ships to be placed
		ships = new ArrayList<>();
		ships.add(new AircraftCarrier());
		ships.add(new Battleship());
		ships.add(new Submarine());
		ships.add(new Destroyer());
		ships.add(new Gunboat());

		shipSelected = ships.get(0);

		shipsLeft = (ArrayList<Ship>) ships.clone();
		info = new Label("                                          ");

		radioButtons = new GridPane();
		shipBoard = new Board(false);// theGame.getUserBoard();
		shipGrid = new GridPane();
		initializePanel();
		setRadioButtonHandlers();
		setButtonHandlers();
		
	}

	private void initializePanel() {
		shipGrid.setVgap(1);
		shipGrid.setHgap(1);

		// adds grid points to GUI
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// shipGrid.add(shipBoard.getPoint(i, j), i, j);
				shipBoard.getPoint(i, j).getStyleClass().add("gridpointDefault");
				shipGrid.add(shipBoard.getPoint(i, j), i, j);
			}
		}

		this.setCenter(shipGrid);
		shipGrid.setAlignment(Pos.CENTER);
		shipGrid.setPadding(new Insets(200,0, 0, 0));

		// set radio buttons and toggle groups
		shipType = new ToggleGroup();
		orientation = new ToggleGroup();

		sub = new RadioButton();
		carrier = new RadioButton("  ");
		destroyer = new RadioButton();
		gunboat = new RadioButton();
		battleship = new RadioButton();
		horiz = new RadioButton();
		vert = new RadioButton();

		sub.setToggleGroup(shipType);
		destroyer.setToggleGroup(shipType);
		carrier.setToggleGroup(shipType);
		gunboat.setToggleGroup(shipType);
		battleship.setToggleGroup(shipType);

		horiz.setToggleGroup(orientation);
		vert.setToggleGroup(orientation);

		radioButtons.setVgap(15);
		radioButtons.add(new Label("    "), 0, 0);
		radioButtons.add(new Label("                                            "), 1, 0);
		radioButtons.add(new Label("    "), 2, 0);
		radioButtons.add(carrier, 0, 1);
		radioButtons.add(battleship, 0, 2);
		radioButtons.add(sub, 0, 3);
		radioButtons.add(destroyer, 0, 4);
		radioButtons.add(gunboat, 0, 5);
		radioButtons.add(new Label("           "), 0, 6);
		radioButtons.add(horiz, 0, 8);
		radioButtons.add(vert, 0, 9);
		
		radioButtons.setPadding(new Insets(232, 110, 50, 80));

		// Set default selection for buttons
		carrier.setSelected(true);
		horiz.setSelected(true);

		this.setRight(radioButtons);
		GridPane.setHalignment(carrier, HPos.CENTER);
		// For formatting the info label
		GridPane i = new GridPane();
	    errormsg.setVisible(true); 
	    errormsg.setManaged(false);
		this.setTop(i);
		info.setAlignment(Pos.TOP_RIGHT);

		// For formatting button
		startGame = new Button("000000000");
		GridPane button = new GridPane();
		button.add(startGame, 0, 2);
		button.add(errormsg, 0, 0);
		button.getStyleClass().add("battleshipFontLrg");
		//top, right, bottom, left
		button.setPadding(new Insets(60, 100, 85, 125));
		button.setPrefSize(100, 200);
		this.setBottom(button);
		button.setAlignment(Pos.BOTTOM_CENTER);
		this.getStyleClass().add("shipSelectorBackground");
		
		
		//startGame.getStyleClass().add("invis");
	}
	
	
	private void setRadioButtonHandlers() {
		carrier.setOnAction(event -> {
			shipSelected = ships.get(0);
		});
		battleship.setOnAction(event -> {
			shipSelected = ships.get(1);
		});
		sub.setOnAction(event -> {
			shipSelected = ships.get(2);
		});
		destroyer.setOnAction(event -> {
			shipSelected = ships.get(3);
		});
		gunboat.setOnAction(event -> {
			shipSelected = ships.get(4);
		});
		horiz.setOnAction(event -> {
			orientationSelected = 0;
		});
		vert.setOnAction(event -> {
			orientationSelected = 1;
		});
		
		startGame.setOnMousePressed(event -> {
			if (restarted) {
				shipGrid.getChildren().removeAll();
				theGame.setView(3);
				theGame.restartGame();
			}
			else if(shipsLeft.size() == 0) {
				System.out.println("Start");
				theGame.setPlayerBoard(shipBoard);
				theGame.setView(2);
				theGame.notifyObservers(theGame);
				
				
				// Prevents buttons from being pressed in
				// game due to lambdas
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						Button temp =shipBoard.getPoint(i, j);
						resetLambdas(temp);
					}
				}
				restarted = true;
			}
			else {
				showErrorMessage(1);
			}
			
		});
	}
	
	private void resetLambdas(Button b) {
		b.setOnMousePressed(event -> {});
	}
	
	private void setButtonHandlers() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Button currButton = shipBoard.getPoint(i, j);
				GridPoint currGP = shipBoard.getGridPoint(i, j);
				register(currButton, currGP, i, j);
			}
		}
	}
	

	
	private void register(Button b, GridPoint gp, int col, int row) {
		b.setOnMousePressed(event -> {

			// horizontal placement of ship
			if (orientationSelected == 0) {
				ImageView shipImage = shipSelected.getShipImageH();
				boolean scrubbed = false;
				
				// makes sure is inbounds
				if (col + shipSelected.size() <= 8) {

					if (!shipsLeft.contains(shipSelected)) {
						scrub(shipSelected);
						System.out.println("Ship going to be erased");
						scrubbed = true;
					}

					// Checks if there is a ship in the current path
					boolean taken = false;
					for (int k = col; k < col + shipSelected.size(); k++) {
						if (shipBoard.getGridPoint(k, row).getShip() != null && !shipBoard.getGridPoint(k, row).getShip().name().equals(shipSelected.name())) {
							taken = true;
							
							if(scrubbed) {
								shipsLeft.add(shipSelected);
							}
							break;
							
							
						}
					}

					// colors all squares with a ship
					if (!taken) {
						for (int k = col; k < col + shipSelected.size(); k++) {
							shipBoard.getGridPoint(k, row).setShip(shipSelected);
							//shipBoard.getGridPoint(k, row).getButton().setStyle("-fx-background-color: #808080; ");
						}
						shipsLeft.remove(shipSelected);
						// working here to update ship placement
					    updateShipPlacement(shipSelected, col, row, true, shipImage);

						shipImage.setFitWidth(34 * shipSelected.size());
			            shipImage.setFitHeight(34);
			            if (shipGrid.getChildren().contains(shipSelected.getShipImageH())) {
			                shipGrid.getChildren().remove(shipSelected.getShipImageH());
			            }
			            if (shipGrid.getChildren().contains(shipSelected.getShipImageV())) {
			                shipGrid.getChildren().remove(shipSelected.getShipImageV());
			            }
			            shipGrid.add(shipImage, col, row, shipSelected.size(), 1);
					}		
				} else {
					showErrorMessage(0);
				}
			} 
			// vertical placement of a ship
			else {
				ImageView shipImage = shipSelected.getShipImageV();
				if (row + shipSelected.size() <= 8) {
					boolean scrubbed = false;
					
					// removes previous instance of ship
					if (!shipsLeft.contains(shipSelected)) {
						scrub(shipSelected);
						System.out.println("Ship going to be erased");
						scrubbed = true;
					}

					// checks if there is a ship in the current path
					boolean taken = false;
					for (int k = row; k < row + shipSelected.size(); k++) {
						if (shipBoard.getGridPoint(col, k).getShip() != null && !shipBoard.getGridPoint(col, k).getShip().name().equals(shipSelected.name())) {
							taken = true;
							// adds ship back into shipsLeft if scrubbed
							// since previous instance removed
							if(scrubbed) {
								shipsLeft.add(shipSelected);
							}
							break;
						}
					}

					// colors all squares with a ship
					if (!taken) {
						for (int k = row; k < row + shipSelected.size(); k++) {
							shipBoard.getGridPoint(col, k).setShip(shipSelected);
							//shipBoard.getGridPoint(col, k).getButton().setStyle("-fx-background-color: #808080; ");
						}
						shipsLeft.remove(shipSelected);

						// working here with updating ship placement
					    updateShipPlacement(shipSelected, col, row, false, shipImage);
						shipImage.setFitWidth(34);
			            shipImage.setFitHeight(34 * shipSelected.size());
			            if (shipGrid.getChildren().contains(shipSelected.getShipImageH())) {
			                shipGrid.getChildren().remove(shipSelected.getShipImageH());
			            }
			            if (shipGrid.getChildren().contains(shipSelected.getShipImageV())) {
			                shipGrid.getChildren().remove(shipSelected.getShipImageV());
			            }
			            shipGrid.add(shipImage, col, row, 1, shipSelected.size()); 
					}	
				} else {
					showErrorMessage(0);
				}
			}			
		});
	}
	
	/**
	 * If the ship is already on the board, we update the coordinates of the 
         * ship on the board
	 * 
	 * @param the current ship and it's associated image, the row and column of the board, 
  	 * and whether or not the ship is horizontal
	 */
	private void updateShipPlacement(Ship ship, int row, int col, boolean isHorizontal, ImageView shipImage) {
		ArrayList<ShipPlacement> shipPlacements = theGame.getShipPlacements();
		boolean found = false;
	    for (ShipPlacement placement : shipPlacements) {
	        if (placement.getShip().equals(ship) && placement.getStartRow() <= 7 && placement.getStartRow() >= 0 && placement.getStartCol() <= 7 && placement.getStartCol() >= 0) {
	            placement.setStartRow(row);
	            placement.setStartCol(col);
	            placement.setIsHorizontal(isHorizontal);
	            found = true;
	            System.out.println("Changing " + ship.name() + " points to " + placement.getStartRow() + " " + placement.getStartCol());
	            System.out.println("Changing " + ship.name() + " points to " + row + " " + col);
	            break; 
	        }
	    }
	    if (!found) {
	        shipPlacements.add(new ShipPlacement(ship, row, col, isHorizontal));
	    }
	}
	
	/**
	 * Shows the error messages for the ship selector view
	 * @param code - and int
	 */
	public void showErrorMessage(int code) {
		if (code == 0) {
			errormsg = new Label("Ship cannot be placed there! Please retry.");
		}
		else if (code == 1) {
			errormsg = new Label("You must place all your ships before continuing");
		}
		errormsg.setVisible(true);
	    errormsg.setManaged(true);
	    Popup errorPopup = new Popup();
	    errorPopup.getContent().add(errormsg);
		errorPopup.show(BattleshipGUI.getStage());
	    System.out.println("Error message visibility: " + errormsg.isVisible());

	    FadeTransition ft = new FadeTransition(Duration.millis(2500), errormsg);
	     errormsg.getStyleClass().add("SSerrorLabel");
	     ft.setFromValue(1.0);
	     ft.setToValue(0.0);
	     ft.setCycleCount(1);
	 
	     ft.play();
	}

	private void scrub(Ship ship) {
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            GridPoint currGP = shipBoard.getGridPoint(i, j);
	            if (currGP.getShip() != null && currGP.getShip().name().equals(ship.name())) {
	                Button currB = shipBoard.getPoint(i, j);
					currB.getStyleClass().add("gridpointDefault");
	                
	                currB.setGraphic(null);
	                currGP.setShip(null);
	            }
	        }
	    }
	}
	
	
	/**
	 * Returns the GridPane with the buttons that make up the board
	 * @return a GridPane of buttons
	 */
	public GridPane shipGrid() {
		return shipGrid;
	}
	

	@Override
	public void update(Object theObserved) {
		System.out.println("update called from Observable BattleshipGame " + theGame);
	}
}
