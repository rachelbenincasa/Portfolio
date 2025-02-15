package view_controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Model.BattleshipGame;
import Model.Board;
import Model.GridPoint;
import Model.Observer;
import Model.ShipPlacement;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Duration;

/**
 * This class handles the display of the player and CPU boards
 * and the switching from player to CPU moves.
 * @author Bronson Housmans, Cole Hersh, Emma Elliott
 */
public class GameView extends BorderPane implements Observer{
	
	private Board playersBoard;
	private Board opponentsBoard;
	private GridPane playersGrid;
	private GridPane opponentsGrid;
	private BattleshipGame theGame;
	private boolean playerTurn;
	private int numUserGuesses;
	private Popup won;
	private GridPane pane;
	private VBox shipTrackerPane;
	private HBox infoPane;
	private Label numCompShips;
	private Label numUserShips;
	private Label gameEvent;
	private boolean gameOver = false;
	
	// used for salvo mode
	private int cpuShots = 0;
	
	/**
	 * Makes an new game view object
	 * @param theModel - the game logic
	 */
	public GameView(BattleshipGame theModel) {
		theGame = theModel;
		numUserGuesses = 1;
		initializePanel();
		theGame.songPlayer.addObserver(this);
	}
	

	private void initializePanel() {
		playerTurn = true;
		pane = new GridPane();
		opponentsBoard = new Board(true);
		//playersBoard = new Board(false);
		playersGrid = new GridPane();
		opponentsGrid = new GridPane();
		
		
		infoPane = new HBox(32);
		shipTrackerPane = new VBox(5);
		shipTrackerPane.setPadding(new Insets(52, 0, 0, 255));
		numCompShips = new Label("        5");
		numCompShips.getStyleClass().add("battleshipFontLrg");
		numCompShips.setTextFill(Color.WHITE);
		numUserShips = new Label("5");
		numUserShips.getStyleClass().add("battleshipFontLrg");
		numUserShips.setTextFill(Color.WHITE);
		
		gameEvent = new Label("User to move");
		gameEvent.setPadding(new Insets(65, 0, 0, 0));
		gameEvent.getStyleClass().add("battleshipFontSmall");
		gameEvent.setTextFill(Color.WHITE);
		gameEvent.setPrefSize(230, 100);
		gameEvent.setWrapText(true);
		
		shipTrackerPane.getChildren().addAll(numCompShips, numUserShips);
		infoPane.getChildren().addAll(shipTrackerPane, gameEvent);
		
		this.setCenter(infoPane);
		// theGame.getUserShips
		// theGame.getCompShips
//		shipTrackerPane.setTop(numCompShips);
//		shipTrackerPane.setBottom(numUserShips);
//		shipTrackerPane.setMinHeight(400);
//		shipTrackerPane.setPadding(new Insets(150, 0, 0, 15));
//		pane.add(shipTrackerPane, 0, 200);
		
		
		setUpGameBoard();
		theGame.startGame(opponentsBoard);
		setRegisters();
//		userWonPopup(new boolean[] {false, true});
	}
    


	private void setUpGameBoard() {
		playersGrid.setHgap(2);
		playersGrid.setVgap(2);
		// top, right, bottom, left
		playersGrid.setPadding(new Insets(25, 150, 0, 225));
		
		opponentsGrid.setHgap(2);
		opponentsGrid.setVgap(2);
		opponentsGrid.setPadding(new Insets(0, 150, 75, 225));
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				opponentsBoard.getGridPoint(i, j).resize();
				opponentsGrid.add(opponentsBoard.getPoint(i,  j), i, j);

			}
		}
		
		pane.add(opponentsGrid, 0, 0);
		opponentsGrid.setPadding(new Insets(150, 0, 0, 200));
		pane.add(playersGrid, 0, 0);
		playersGrid.setPadding(new Insets(0, 0, 25, 200));
		this.setTop(opponentsGrid);
		this.setBottom(playersGrid);
		this.getStyleClass().add("gameViewBackground");
		
	}
	
	/**
	 * Configures the images of ships on the users board based on their ship
	 * placement in the shipPlacement view, displays each image on the board
	 * 
	 */
	public void setUserBoard() {
		// gets the ship placements the user chose
		ArrayList<ShipPlacement> shipPlacements = theGame.getShipPlacements();
		playersBoard = theGame.getUserBoard();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				playersGrid.add(playersBoard.getPoint(i, j), i, j);
				GridPoint gp = playersBoard.getGridPoint(i, j);
				gp.resize();
				// info about each ship is stored in each gridPoint
				Button currButton = gp.getButton();
				currButton.toBack();
				if (!gp.getShipName().equals("Empty"))
					//currButton.setStyle("-fx-background-color: #808080; ");
				for (ShipPlacement placement : shipPlacements) {
					if (i == placement.getStartRow() && j == placement.getStartCol()) {
						// places each ship in its appropriate position on the board
						ImageView shipImage = placement.getShipImage();
						if (placement.isHorizontal()) {
							shipImage.setFitWidth(30 * placement.getShip().size());
							shipImage.setFitHeight(30);
							// add the image here for horizontal ship
							System.out.println("Row: " + i + " Col: " + j);
							playersGrid.add(shipImage, i, j);
							shipImage.toFront();
						} else {
							shipImage.setFitWidth(30);
							shipImage.setFitHeight(30 * placement.getShip().size());
							// add the image here for the vertical ship
							playersGrid.add(shipImage, i, j);
							shipImage.toFront();
						}
						shipPlacements.remove(placement);
						break;
					}
				}
			}
		}
	}
	
	private void setRegisters() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Button currButton = opponentsBoard.getPoint(i, j);
				GridPoint currGP = opponentsBoard.getGridPoint(i, j);
				registerHelper(currButton, currGP);
			}
		}
	}
	
	private void registerHelper(Button button, GridPoint currGP) {
		button.setOnMousePressed(event -> {
			if (!currGP.guessed() && playerTurn && theGame.songPlayer.isOver()) {
				
				Button curr = (Button) event.getSource();
				currGP.setGuess(true);
				if (!currGP.getShipName().equals("Empty")) {
					// turns color to red if ship was hit
					curr.setStyle("-fx-background-color: #d2042d; ");
					theGame.songPlayer.playSong(theGame.getHitSound());
					hitAnimation(currGP, opponentsGrid);
					gameEvent.setText("Hit by user at (" + currGP.getRow() + ", " + currGP.getCol() + ")");
					
					currGP.getShip().addHit();
					if (currGP.getShip().checkSunk()) {
						theGame.decrementCompShips();
						theGame.songPlayer.playSong("/shipAlarm.mp3");
						gameEvent.setText("You sunk their " + currGP.getShip().name() + "!");
					}
				} else {
					// turns color to white if ship was not hit
					missAnimation(currGP, opponentsGrid);
					curr.setStyle("-fx-background-color: #ffffff; ");
					theGame.songPlayer.playSong("/miss.mp3");
					gameEvent.setText("Miss by user at (" + currGP.getRow() + ", " + currGP.getCol() + ")");
				}

				if (theGame.getGameMode() && numUserGuesses == theGame.getUserShips()) {
					playerTurn = false;
					numUserGuesses = 1;
					
				} else if (!theGame.getGameMode()) {
					playerTurn = false;
					//handleComputerMove();
				
				
					
				} else if (theGame.getGameMode()) {
					numUserGuesses++;
				}
				checkGameOver();
			}
			numUserShips.setText(String.valueOf(theGame.getUserShips()));
		});
	}
	
	
	private void checkGameOver() {
		boolean[] currResults = theGame.isGameOver();
		if (currResults[0] || currResults[1]) {
			gameOver = true;
			// keeps CPU from shooting if shots left
			cpuShots = 5;
			// the players has won
			System.out.println("Game is over");
			userWonPopup(currResults);
		}
	}
	
	private void userWonPopup(boolean[] currResults) {
		BorderPane winContent = new BorderPane();
		won = new Popup();
		Button exit = new Button("Exit");
		exit.getStyleClass().add("MMbutton");
		Button again = new Button("Play again");
		again.getStyleClass().add("MMbutton");
		setEndGameHandlers(exit, again);
		if (currResults[0]) {
			winContent.getStyleClass().add("winPopupBackground");
			theGame.setFx("/win.mp3");
		}
		else {
			winContent.getStyleClass().add("losePopupBackground");
			theGame.setFx("/lose.mp3");
		}
		HBox options = new HBox(210);
		options.setPadding(new Insets(340, 10, 0, 20));
		options.getChildren().addAll(exit, again);
		//swinContent.setBottom(options);
		won.getContent().addAll(winContent, options);
		winContent.setPrefSize(394, 394);
		won.setHeight(394);
		won.setWidth(394);
		won.show(BattleshipGUI.getStage());
		System.out.println("win popup showing");
	}
	
	private void setEndGameHandlers(Button exit, Button again) {
		
		exit.setOnAction(event -> {
			BattleshipGUI.getStage().close();
			won.hide();
		});
		
		again.setOnAction(event -> {
			won.hide();
			theGame.setRestart(true);
			
			
		});
	}
	
	/**
	 * Returns the Winning/Losing popup object
	 * @return a popup object
	 */
	public Popup getPopup() {
		return won;
	}
	
	
	
	private void handleComputerMove() {
		if(!playerTurn && theGame.songPlayer.isOver() && !theGame.getGameMode() && !gameOver) {
			theGame.compMove();
			if(theGame.compMoveHit()) {
				hitAnimation(theGame.lastCompMove(), playersGrid);
				gameEvent.setText("Hit by computer at (" + theGame.lastCompMove().getRow() + ", " + theGame.lastCompMove().getCol() + ")");
				if(theGame.lastCompMove().getShip().checkSunk()) {
					gameEvent.setText("The computer has sunk your " + theGame.lastCompMove().getShip().name() + "!");
				}
			} else {
				missAnimation(theGame.lastCompMove(), playersGrid);
				gameEvent.setText("Miss by computer at (" + theGame.lastCompMove().getRow() + ", " + theGame.lastCompMove().getCol() + ")");
			}
			playerTurn = true;
		}
		else if(!playerTurn && theGame.songPlayer.isOver() && theGame.getGameMode() && !gameOver) {
				if(cpuShots == theGame.getCompShips()) {
					playerTurn = true;
					cpuShots = 0;
				}
				else {
					theGame.compMove();
					if(theGame.compMoveHit()) {
						hitAnimation(theGame.lastCompMove(), playersGrid);
						gameEvent.setText("Hit by computer at (" + theGame.lastCompMove().getRow() + ", " + theGame.lastCompMove().getCol() + ")");
						if(theGame.lastCompMove().getShip().checkSunk()) {
							gameEvent.setText("The computer has sunk your " + theGame.lastCompMove().getShip().name() + "!");
						}
					} else {
						missAnimation(theGame.lastCompMove(), playersGrid);
						gameEvent.setText("Miss by computer at (" + theGame.lastCompMove().getRow() + ", " + theGame.lastCompMove().getCol() + ")");
					}
					cpuShots++;
				}
				checkGameOver();
		}
		numCompShips.setText("          " + theGame.getCompShips());
	}


	@Override
	public void update(Object theObserved) {
		// makes sure to update game if it is the songPlayer
		if(theObserved.getClass().equals(theGame.songPlayer.getClass())) {
			handleComputerMove();
		}
		System.out.println("update called from Observable BattleshipGame " + theGame);
		
	}
	
	private void hitAnimation(GridPoint currGP, GridPane pane) {
		Image explosion1 = new Image("explosion1.jpg", 30, 30, false, false);
		ImageView image1 = new ImageView(explosion1);
		Image explosion2 = new Image("explosion2.jpg", 30, 30, false, false);
		ImageView image2 = new ImageView(explosion2);
		Image explosion3 = new Image("explosion3.jpg", 30, 30, false, false);
		ImageView image3 = new ImageView(explosion3);
		Image explosion4 = new Image("explosion4.jpg", 30, 30, false, false);
		ImageView image4 = new ImageView(explosion4);
		Image explosion5 = new Image("explosion5.jpg", 30, 30, false, false);
		ImageView image5 = new ImageView(explosion5);
		pane.add(image1, currGP.getRow(), currGP.getCol());
		image1.toFront();
		Timeline timeline = new Timeline(
	            new KeyFrame(Duration.ZERO, e -> {
	                // nothing at time 0
	            }),
	            new KeyFrame(Duration.seconds(0.2), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image1);
	                pane.add(image2, currGP.getRow(), currGP.getCol());
	                image2.toFront();
	            }),
	            new KeyFrame(Duration.seconds(0.4), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image2);
	                pane.add(image3, currGP.getRow(), currGP.getCol());
	                image3.toFront();
	            }),
	            new KeyFrame(Duration.seconds(0.6), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image3);
	                pane.add(image4, currGP.getRow(), currGP.getCol());
	                image4.toFront();
	            }),
	            new KeyFrame(Duration.seconds(0.8), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image4);
	                pane.add(image5, currGP.getRow(), currGP.getCol());
	                image5.toFront();
	            }),
	            new KeyFrame(Duration.seconds(1), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image5);
	            })
	    );
		timeline.play();
	}
	
	private void missAnimation(GridPoint currGP, GridPane pane) {
		
		Image miss1 = new Image("splash1.jpg", 30, 30, false, false);
		ImageView image1 = new ImageView(miss1);
		Image miss2 = new Image("splash2.jpg", 30, 30, false, false);
		ImageView image2 = new ImageView(miss2);
		Image miss3 = new Image("splash3.jpg", 30, 30, false, false);
		ImageView image3 = new ImageView(miss3);
		Image miss4 = new Image("splash4.jpg", 30, 30, false, false);
		ImageView image4 = new ImageView(miss4);
		Image miss5 = new Image("splash5.jpg", 30, 30, false, false);
		ImageView image5 = new ImageView(miss5);
		pane.add(image1, currGP.getRow(), currGP.getCol());
		image1.toFront();
		Timeline timeline = new Timeline(
	            new KeyFrame(Duration.ZERO, e -> {
	                // nothing at time 0
	            }),
	            new KeyFrame(Duration.seconds(0.2), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image1);
	                pane.add(image2, currGP.getRow(), currGP.getCol());
	                image2.toFront();
	            }),
	            new KeyFrame(Duration.seconds(0.4), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image2);
	                pane.add(image3, currGP.getRow(), currGP.getCol());
	                image3.toFront();
	            }),
	            new KeyFrame(Duration.seconds(0.6), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image3);
	                pane.add(image4, currGP.getRow(), currGP.getCol());
	                image4.toFront();
	            }),
	            new KeyFrame(Duration.seconds(0.8), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image4);
	                pane.add(image5, currGP.getRow(), currGP.getCol());
	                image5.toFront();
	            }),
	            new KeyFrame(Duration.seconds(1), e -> {
	                // Remove the image view from the grid pane
	                pane.getChildren().remove(image5);
	            })
	    );
		timeline.play();
	}

}