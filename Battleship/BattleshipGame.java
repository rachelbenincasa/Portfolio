package Model;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
/** 
 * This class handles the creation and updating of the two boards
 * along with getting the CPU's move
 * @author Bronson Housmans
 * @author Cole Hersh
 * @author Rachel Benincasa
 */
public class BattleshipGame extends Observable {

	private Board computerBoard;
	private ComputerPlayer compPlayer;
	private Board userBoard;
	private AircraftCarrier compAC;
	private Battleship compBattleship;
	private Destroyer compDestroyer;
	private Gunboat compGunboat;
	private Submarine compSub;
	private ArrayList<Ship> shipList;
	private Random generator = new Random();
	private int viewDeterminer;
	private ArrayList<ShipPlacement> shipPlacements;
	private boolean isSalvoMode;
	private int userShipsLeft;
	private int compShipsLeft;
	private String hitSound;
	private boolean restart;
	private GridPoint lastCompMove;
	private boolean compMoveHit;
	
	/**
	 * The SongPlayerQueue that plays soundFX then notifies the GameView
	 */
	public SongPlayerQueue songPlayer;
	
	private SongPlayer gameMusic;

	


	/**
	 * initializes new game with a new computer player and 2 boards for user and
	 * computer may need to create parameters for if we want to have 2 real people
	 * play each other
	 */
	public BattleshipGame() {
		gameMusic = new SongPlayer();
		restart = false;
		shipPlacements = new ArrayList<>();
		viewDeterminer = 0;
		shipList = new ArrayList<>();
		computerBoard = new Board(true);
		compPlayer = new ComputerPlayer();
		userBoard = new Board(false);
		notifyObservers(this);
		compAC = new AircraftCarrier();
		compBattleship = new Battleship();
		compDestroyer = new Destroyer();
		compGunboat = new Gunboat();
		compSub = new Submarine();
		shipList.add(compAC);
		shipList.add(compBattleship);
		shipList.add(compDestroyer);
		shipList.add(compGunboat);
		shipList.add(compSub);
		userShipsLeft = 5;
		compShipsLeft = 5;
		
		
		songPlayer = new SongPlayerQueue();
		
	}
	
	/**
	 * Returns the hitsound file string
	 * @return a string 
	 */
	public String getHitSound() {
		return hitSound;
	}
	
	/**
	 * Gets if the game should be restarted
	 * @return true if yes and false otherwise
	 */
	public boolean getRestart() {
		return restart;
	}
	
	/**
	 * Called on a restart. Resets variable
	 * @param rest - a boolean that is true if is to restart
	 */
	public void setRestart(boolean rest) {
		restart = rest;
		notifyObservers(this);
	}
	
	/**
	 * Sets the view so that the BattleshipGUI cal pull it
	 * and change the view.  Ex: the startgame button in the main menu
	 * @param v - and int
	 */
	public void setView(int v){
		viewDeterminer = v;
	}
	
	/**
	 * Sets and plays the game music
	 * @param song - a string taht is the song file
	 */
	public void playMusic(String song) {
		gameMusic.playSong(song);
	}
	
	/**
	 * selects the hit song to use
	 * @param n - and int that is 1 or explosion and 2 for taco bell
	 */
	public void chooseHit(int n) {
		if (n == 1) {
			// set hit sound to explosion
			hitSound = "/explosion.mp3";
		} else if (n == 2) {
			// set hit sound to taco bell 
			hitSound = "/tacobell.mp3";
		}
	}
	
	
	/**
	 * Returns the current view
	 * @return an int that represents which view the game should display
	 */
	public int getView() {
		return viewDeterminer;
	}
	
	// this method will allow the computer player to set the placement of their ships
	private void setCompBoard() {
		int availableSpots = 5;
		for (int i = 0; i < 5; i++) {
			int currIndex = generator.nextInt(availableSpots);
			Ship currShip = shipList.remove(currIndex);
			availableSpots--;

			int xVal = generator.nextInt(8);
			int yVal = generator.nextInt(8);
			int direction = generator.nextInt(4);
			setCompHelper(xVal, yVal, direction, currShip);
		}
	}

	private void setCompHelper(int xVal, int yVal, int direction, Ship currShip) {
		GridPoint currGP;
		int availableSpots = 0;
		boolean noPlacement = true;
		// place ships going down from starting position
		if (direction == 0) {
			for (int i = yVal; i < yVal + currShip.size(); i++) {
				if (i > 7) {
					break;
				}
				currGP = computerBoard.getGridPoint(xVal, i);
				if (currGP.getShipName().equals("Empty")) {
					availableSpots++;
				}
			}
			if (availableSpots == currShip.size()) {
				for (int i = yVal; i < yVal + currShip.size(); i++) {
					currGP = computerBoard.getGridPoint(xVal, i);
					currGP.setShipName(currShip.name());
					currGP.setShip(currShip);
				}
				noPlacement = false;
			}
		}
		// place ships starting at bottom and place upwards vertically
		else if (direction == 1) {
			for (int i = yVal; i > yVal - currShip.size(); i--) {
				if (i < 0 || i > 7) {
					break;
				}
				currGP = computerBoard.getGridPoint(xVal, i);
				if (currGP.getShipName().equals("Empty")) {
					availableSpots++;
				}
			}
			if (availableSpots == currShip.size()) {
				for (int i = yVal; i > yVal - currShip.size(); i--) {
					currGP = computerBoard.getGridPoint(xVal, i);
					currGP.setShipName(currShip.name());
					currGP.setShip(currShip);
				}
				noPlacement = false;
			}
		}
		// place ships left to right
		else if (direction == 2) {
			for (int i = xVal; i < xVal + currShip.size(); i++) {
				if (i > 7 || i < 0) {
					break;
				}
				currGP = computerBoard.getGridPoint(i, yVal);
				if (currGP.getShipName().equals("Empty")) {
					availableSpots++;
				}
			}
			if (availableSpots == currShip.size()) {
				for (int i = xVal; i < xVal + currShip.size(); i++) {
					currGP = computerBoard.getGridPoint(i, yVal);
					currGP.setShipName(currShip.name());
					currGP.setShip(currShip);
				}
				noPlacement = false;
			}
		}
		// place ship right to left
		else {
			for (int i = xVal; i > xVal - currShip.size(); i--) {
				if (i < 0 || i > 7) {
					break;
				}
				currGP = computerBoard.getGridPoint(i, yVal);
				if (currGP.getShipName().equals("Empty")) {
					availableSpots++;
				}
			}
			if (availableSpots == currShip.size()) {
				for (int i = xVal; i > xVal - currShip.size(); i--) {
					currGP = computerBoard.getGridPoint(i, yVal);
					currGP.setShipName(currShip.name());
					currGP.setShip(currShip);
				}
				noPlacement = false;
			}
		}
		// recursive call on method with new parameters if the ship was not placed
		if (noPlacement) {
			xVal = generator.nextInt(8);
			yVal = generator.nextInt(8);
			direction = generator.nextInt(4);
			setCompHelper(xVal, yVal, direction, currShip);
		}
	}
	
	
	/**
	 * Determines when the game is over and who one
	 * @return a 2 element boolean array with the playerwin and cpuwin booleans
	 */
	public boolean[] isGameOver() {
		boolean playerWon = false;
		boolean compWon = false;
		if (userShipsLeft == 0) {
			compWon = true;
		}
		if (compShipsLeft == 0) {
			playerWon = true;
		}
		return new boolean[] {playerWon, compWon};
	}

	

	/**
	 * should be called after the user sets their ships and GUI changes to gameView from shipSelector
	 * Will set the placement of the ships for the computer
	 * @param computerBoardIn - a board object that is the cpu's ships
	 */
	public void startGame(Board computerBoardIn) {
		computerBoard = computerBoardIn;
		//userBoard = userBoardIn;
	    applyShipPlacementsToBoard(userBoard);
		setCompBoard();
		notifyObservers(this);
	}
	
	/**
	 * Returns the ShipPlacement objects for each ship
	 * For displaying the ship images on the player's board
	 * @return a ShipPlacement ArrayList
	 */
	public ArrayList<ShipPlacement> getShipPlacements() {
		return shipPlacements;
	}
	
	/**
	 * Resets the ship placements and images
	 */
	public void resetShipPlacements() {
		shipPlacements.clear();
	}
	
	/**
	 * Resets the game music so a new one can be played
	 */
	public void resetMusic() {
		gameMusic.dispose();
	}
	
	/**
	 * Places the ship images on the Player's board
	 * @param board - a board object (which is always the player's)
	 */
	public void applyShipPlacementsToBoard(Board board) {
	    for (ShipPlacement placement : shipPlacements) {
	        Ship ship = placement.getShip();
	        int startRow = placement.getStartRow();
	        int startCol = placement.getStartCol();
	        boolean isHorizontal = placement.isHorizontal();
	        ImageView shipImage = isHorizontal ? ship.getShipImageH() : ship.getShipImageV();
	        if (isHorizontal) {
            	shipImage.setFitWidth(34 * placement.getShip().size());
	            shipImage.setFitHeight(34);
            } else {
            	shipImage.setFitWidth(34);
	            shipImage.setFitHeight(34 * placement.getShip().size());
            }
	        System.out.println(shipImage.getFitHeight());
	        System.out.println(shipImage.getFitWidth());
	        
	        for (int i = 0; i < ship.size(); i++) {
	            int row = isHorizontal ? startRow : startRow + i;
	            int col = isHorizontal ? startCol + i : startCol;
	            
	            // I CANT FIGURE IT OUT :O plz help
	            GridPoint point = board.getGridPoint(row, col);
	            point.setShip(ship);
	            Button button = point.getButton();
	            button.setGraphic(new ImageView(shipImage.getImage()));
	        }
	    }
	}

	/**
	 * allows user to select the AI to play against
	 * @param inputStrat - a BattleshipStrategy object
	 */
	public void setDifficulty(BattleshipStrategy inputStrat) {
		compPlayer.setDifficulty(inputStrat);
	}

	/**
	 * returns the current AI that is being used
	 * @return - type ComputerPlayer
	 */
	public ComputerPlayer getCompPlayer() {
		return compPlayer;
	}

	/**
	 * returns the board for the computer player
	 * @return the cpu's board object
	 */
	public Board getCompBoard() {
		return computerBoard;
	}

	/**
	 * returns the board for the human player
	 * @return the player's board object
	 */
	public Board getUserBoard() {
		return userBoard;
	}
	
	/**
	 * Sets the players board.  Called by the ShipSelectorView
	 * @param b - a baord object
	 */
	public void setPlayerBoard(Board b) {
		userBoard = b;
	}
	
	
	
	/**
	 * makes a move based on the compPlayer and updates the board
	 */
	public void compMove() {
		GridPoint currMove = compPlayer.guessSpot(userBoard);
		lastCompMove = currMove;
		String spotName = currMove.getShipName();
		// if ship is hit
		if (!currMove.guessed() && !spotName.equals("Empty")) {
			
			currMove.setGuess(true);
			compMoveHit = true;
			currMove.getShip().addHit();
			System.out.println("---------------- Hit  -----------------------");
			
		
			// if ship is sunk
			songPlayer.playSong(hitSound);
			if(currMove.getShip().checkSunk()) {
				userShipsLeft--;
				compPlayer.notifySink();
				
				/*int i =  0;
				while(true) {
				//	System.out.println(songPlayer.isOver());
					if(i % 5 == 0 && songPlayer.isOver()) {
						break;
					}
					i++;
				}*/
				songPlayer.playSong("/shipAlarm.mp3");
			}
			else {
				
			}
			 currMove.getButton().setStyle("-fx-background-color: #d2042d; ");
			
		} else if (!currMove.guessed() && spotName.equals("Empty")) {
			
				System.out.println("---------------- Miss  -----------------------");
				currMove.setGuess(true);
				compMoveHit = false;
				currMove.getButton().setStyle("-fx-background-color: #ffffff; ");
				songPlayer.playSong("/miss.mp3");
			
		}
		else {
			compMove();
		}
	}
	
	/**
	 * Restart the game by resetting all of the values back to their original state
	 */
	public void restartGame() {
		compAC.resetHits();
		compBattleship.resetHits();
		compDestroyer.resetHits();
		compGunboat.resetHits();
		compSub.resetHits();
		
		compShipsLeft = 5;
		userShipsLeft = 5;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				GridPoint currCompGP = computerBoard.getGridPoint(i, j);
				currCompGP.setGuess(false);
				Button compButton = currCompGP.getButton();
				compButton.setStyle("-fx-background-color: #36a8c9");
				GridPoint currUserGP = userBoard.getGridPoint(i, j);
				currUserGP.setGuess(false);
				Button userButton = currUserGP.getButton();
				userButton.setStyle("-fx-background-color: #36a8c9");
			}
		}
		// readds ships
		shipList.add(compAC);
		shipList.add(compBattleship);
		shipList.add(compDestroyer);
		shipList.add(compGunboat);
		shipList.add(compSub);
		restart = false;
		notifyObservers(this);
	}
	
	/**
	 * Sets the game mode to false for regular battleship and true for salvo mode
	 * @param modeInput - a bollean that is true for salvo mode and false for classic
	 */
	public void setGameMode(boolean modeInput) {
		isSalvoMode = modeInput;
	}
	
	/**
	 * Returns true for playing salvo mode and false for normal battleship
	 * @return isSavloMode- boolean
	 */
	public boolean getGameMode() {
		return isSalvoMode;
	}
	
	/**
	 * Lowers the number of user ships by 1
	 */
	public void decrementUserShips() {
		userShipsLeft--;
	}
	
	/**
	 * Returns the number of user ships left
	 * @return userShipsLeft- int
	 */
	public int getUserShips() {
		return userShipsLeft;
	}
	
	/**
	 * Decreases the number of computer ships by 1
	 */
	public void decrementCompShips() {
		compShipsLeft--;
	}
	
	/**
	 * Return the number of computer ships left
	 * @return compShipsLeft- int
	 */
	public int getCompShips() {
		return compShipsLeft;
	}
	
	
	/**
	 * Plays the desired soundFx
	 * @param fx - a path to an aduio file
	 */
	public void setFx(String fx) {
		songPlayer.playSong(fx);
	}
	
	public GridPoint lastCompMove() {
		return lastCompMove;
	}
	
	public boolean compMoveHit() {
		return compMoveHit;
	}
}
