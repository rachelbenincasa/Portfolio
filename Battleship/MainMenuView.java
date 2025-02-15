package view_controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Model.BattleshipGame;
import Model.HardAI;
import Model.Observer;
import Model.RandomAI;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.util.Duration;
/**
 *  This class represents the main menu of the game
 *  with a difficulty setting selection and game mode selection
 *  
 *  Notable: extends BorderPane so switching views is easier
 *  Notable: implements Observer to update the view when switched
 *  
 *  @author Rachel Benincasa
 *  @author Cole Hersh
 */
public class MainMenuView extends BorderPane implements Observer {
	private BattleshipGame theGame;
	private ToggleGroup gameModes;
	private ToggleGroup ai;
	
	/**
	 * Constructs the main menu
	 * @param theModel - The game logic controller class
	 */
	public MainMenuView(BattleshipGame theModel) {
		theGame = theModel;
		theGame.resetMusic();
		theGame.playMusic("/battleship.mp3");
		initializePanel();
	}
	
	/**
	 * Creates the main menu and all of its elements
	 * Sets game difficulty
	 */
	public void initializePanel() {
		
		// button to see info & rules popup
		Button info = new Button("Info & Rules");
		info.getStyleClass().add("MMbutton");
		VBox infoBox = new VBox(10, info);
		this.setBottom(infoBox);
		infoBox.setPadding(new Insets(0, 280, 20, 330));
		info.setOnAction(event -> {
			infoPopup();
		});

	    // AI Selection
	    Label pickAILabel = new Label("\nPick your opponent!");
	    pickAILabel.setTextFill(Color.WHITE);
	    pickAILabel.setWrapText(true);
	    pickAILabel.setTextAlignment(TextAlignment.CENTER);
	    
	    ai = new ToggleGroup();
	    RadioButton randomAIButton = new RadioButton("Random AI");
	    randomAIButton.setTextFill(Color.WHITE);
	    randomAIButton.setToggleGroup(ai);
	    RadioButton intermediateAIButton = new RadioButton("Hard AI");
	    intermediateAIButton.setTextFill(Color.WHITE);
	    intermediateAIButton.setToggleGroup(ai);

	    VBox aiSelectionBox = new VBox(10, pickAILabel, randomAIButton, intermediateAIButton);
	    aiSelectionBox.setAlignment(Pos.CENTER);
	    aiSelectionBox.setPadding(new Insets(185, 0, 0, 25));
	    this.setCenter(aiSelectionBox);
	    pickAILabel.getStyleClass().add("battleshipFontMed");
	    pickAILabel.setTextFill(Color.WHITE);
	    randomAIButton.getStyleClass().add("battleshipFontMed");
	    randomAIButton.setTextFill(Color.WHITE);
	    intermediateAIButton.getStyleClass().add("battleshipFontMed");
	    intermediateAIButton.setTextFill(Color.WHITE);
	    
	    Label songLabel = new Label("\nSelect Song!");
	    songLabel.setTextFill(Color.WHITE);
	    songLabel.setWrapText(true);
	    songLabel.setTextAlignment(TextAlignment.CENTER);
	    
	    ToggleGroup songChoice = new ToggleGroup();
	    RadioButton song1 = new RadioButton("Battleship");
	    song1.setTextFill(Color.WHITE);
	    song1.setToggleGroup(songChoice);
	    RadioButton song2 = new RadioButton("Kirby");
	    song2.setTextFill(Color.WHITE);
	    song2.setToggleGroup(songChoice);

	    VBox gameTypeBox = new VBox(10, songLabel, song1, song2);
	    gameTypeBox.setAlignment(Pos.CENTER);
	    gameTypeBox.getStyleClass().add("invis");
	    gameTypeBox.getStyleClass().add("battleshipFontMed");
	    
	    
	    
	    // Select game mode
	    gameModes = new ToggleGroup();
	    Label selectMode = new Label("\nSelect Game Mode");
	    selectMode.setTextFill(Color.WHITE);
	    RadioButton normal = new RadioButton("Normal Battleship");
	    normal.setTextFill(Color.WHITE);
	    normal.setToggleGroup(gameModes);
	    RadioButton salvo = new RadioButton("Salvo Mode");
	    salvo.setTextFill(Color.WHITE);
	    salvo.setToggleGroup(gameModes);
	    
	    VBox modesBox = new VBox(10, selectMode, normal, salvo);
	    modesBox.setAlignment(Pos.CENTER);
	    modesBox.getStyleClass().add("invis");
	    modesBox.getStyleClass().add("battleshipFontMed");
	    
	    Label hitSoundLabel = new Label("\nSelect Hit Sound!");
	    hitSoundLabel.setTextFill(Color.WHITE);
	    hitSoundLabel.setWrapText(true);
	    hitSoundLabel.setTextAlignment(TextAlignment.CENTER);

	    ToggleGroup hitSoundChoice = new ToggleGroup();
	    RadioButton hitSound1 = new RadioButton("Explosion");
	    hitSound1.setTextFill(Color.WHITE);
	    hitSound1.setToggleGroup(hitSoundChoice);
	    RadioButton hitSound2 = new RadioButton("Taco Bell");
	    hitSound2.setTextFill(Color.WHITE);
	    hitSound2.setToggleGroup(hitSoundChoice);

	    VBox hitSoundBox = new VBox(10, hitSoundLabel, hitSound1, hitSound2);
	    hitSoundBox.setAlignment(Pos.CENTER);
	    hitSoundBox.getStyleClass().add("invis");
	    hitSoundBox.getStyleClass().add("battleshipFontMed");


	    // Start Button
	    Button startButton = new Button("Start");
	    startButton.getStyleClass().add("invisButton");
	    
	    randomAIButton.setOnAction(event -> {
		     transition(aiSelectionBox, gameTypeBox);	     
	    });
	    
	    intermediateAIButton.setOnAction(event -> {
	    	transition(aiSelectionBox, gameTypeBox); 	
	    });
	    
	    song1.setOnAction(event -> {
	    	transition(gameTypeBox, modesBox);
	    });
	    
	    song2.setOnAction(event -> {
	    	transition(gameTypeBox, modesBox);
	    });
	    
	    normal.setOnAction(event -> {
	    	transition(modesBox, hitSoundBox);
	    });
	    
	    salvo.setOnAction(event -> {
	    	transition(modesBox, hitSoundBox);
	    });
	    
	    hitSound1.setOnAction(event -> {
	    	getMode();
	    	setDifficulty();
	        System.out.println("To Ship Selector");
	        theGame.setView(1);
	        theGame.notifyObservers(theGame);
	        RadioButton selectedSong = (RadioButton) songChoice.getSelectedToggle();
	        if (selectedSong != null) {
	            String selectedLabel = selectedSong.getText();
	            System.out.println("Hit sound is ------------------------------ " + selectedLabel);
	            theGame.resetMusic();
	            if ("Battleship".equals(selectedLabel)) {
	           
	            	theGame.playMusic("/battleship.mp3");
	            } else {
	            	theGame.playMusic("/kirbysong.mp3");
	               
	            } 
	        }
	        
	        RadioButton selectedHitSound = (RadioButton) hitSoundChoice.getSelectedToggle();
	        if (selectedHitSound != null) {
	        	String selectedLabel = selectedHitSound.getText();
	        	System.out.println("Hit sound is ------------------------------ " + selectedLabel);
	        	if ("Explosion".equals(selectedLabel)) {
	        		System.out.println("Hit Ex");
	            	
	            	theGame.chooseHit(1);
	            
	            } else if ("Taco Bell".equals(selectedLabel)) {
	            	System.out.println("Hit Bell");
	      
	                theGame.chooseHit(2);
	            }
	        }
	    });
	    
	    hitSound2.setOnAction(event -> {
	    	getMode();
	    	setDifficulty();
	        System.out.println("To Ship Selector");
	        theGame.setView(1);
	        theGame.notifyObservers(theGame);
	        RadioButton selectedSong = (RadioButton) songChoice.getSelectedToggle();
	        if (selectedSong != null) {
	            String selectedLabel = selectedSong.getText();
	            System.out.println("Hit sound is ------------------------------ " + selectedLabel);
	            theGame.resetMusic();
	            if ("Battleship".equals(selectedLabel)) {
	           
	            	theGame.playMusic("/battleship.mp3");
	            } else {
	            	theGame.playMusic("/kirbysong.mp3");
	               
	            } 
	        }
	        
	        RadioButton selectedHitSound = (RadioButton) hitSoundChoice.getSelectedToggle();
	        if (selectedHitSound != null) {
	        	String selectedLabel = selectedHitSound.getText();
	        	System.out.println("Hit sound is ------------------------------ " + selectedLabel);
	        	if ("Explosion".equals(selectedLabel)) {
	        		System.out.println("Hit Ex");
	            	
	            	theGame.chooseHit(1);
	            
	            } else if ("Taco Bell".equals(selectedLabel)) {
	            	System.out.println("Hit Bell");
	      
	                theGame.chooseHit(2);
	            }
	        }
	    });

	    this.getStyleClass().add("mainMenuBackground");
	}
	
	private void transition(VBox from, VBox to) {
		FadeTransition ft = new FadeTransition(Duration.millis(1000), from);
	     ft.setFromValue(1.0);
	     ft.setToValue(0.0);
	     ft.setCycleCount(1);
	     ft.play();
	     ft.setOnFinished(event2 -> {
	    	 FadeTransition ft2 = new FadeTransition(Duration.millis(1000), to);
		     ft2.setFromValue(0.0);
		     ft2.setToValue(1.0);
		     ft2.setCycleCount(1);
		     ft2.play();
			 to.setPadding(new Insets(185, 0, 0, 25));
			 this.setCenter(to);
		     from.setVisible(false);
	     });
	}
	
	
    
    /**
     * updates all observers based on the change
     */
	@Override
	public void update(Object theObserved) {
		System.out.println("update called from Observable BattleshipGame " + theGame);
		
	}
	
	private void setDifficulty() {
		RadioButton selectedDiff = (RadioButton)ai.getSelectedToggle();
		String diffIn = selectedDiff.getText();
		if(diffIn.equals("Random AI")) {
			theGame.setDifficulty(new RandomAI());
		} else {
			theGame.setDifficulty(new HardAI());
		}
	}
	
	private void getMode() {
		RadioButton selectedMode = (RadioButton) gameModes.getSelectedToggle();
		String modeIn = selectedMode.getText();
		if(modeIn.equals("Normal Battleship")) {
			theGame.setGameMode(false);
		} else {
			theGame.setGameMode(true);
		}
	}
	
	@SuppressWarnings("resource")
	private void infoPopup() {
		Font popUpHeader = new Font("Lucida Console", 22);
		Font popUpContent = new Font("Lucida Console", 14);
		BorderPane infoContent = new BorderPane();
		Popup infoPop = new Popup();
		Button exit = new Button("Exit Info & Rules Page");
		exit.getStyleClass().add("MMbutton");
		VBox exitButton = new VBox(10, exit);
		exitButton.setPadding(new Insets(0, 150, 15, 150));
		infoContent.setBottom(exitButton);
		
		exit.setOnAction(event -> {
			infoPop.hide();
		});
		
		File textIn = new File("src/info.txt");
		Scanner scan = new Scanner(System.in);
		try {
			scan = new Scanner(textIn);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String infoLine = scan.nextLine();
		Label infoLabel = new Label(scan.nextLine());
		infoLabel.setFont(popUpHeader);
		infoLabel.setTextFill(Color.YELLOW);
		//String infoLineContent = scan.nextLine();
		Label infoText = new Label(scan.nextLine());
		infoText.setWrapText(true);
		infoText.setFont(popUpContent);
		infoText.setTextFill(Color.YELLOW);
		Label rulesLabel = new Label(scan.nextLine());
		rulesLabel.setFont(popUpHeader);
		rulesLabel.setTextFill(Color.YELLOW);
		Label rulesText = new Label(scan.nextLine());
		rulesText.setWrapText(true);
		rulesText.setFont(popUpContent);
		rulesText.setTextFill(Color.YELLOW);
		Label modeLabel = new Label(scan.nextLine());
		modeLabel.setFont(popUpHeader);
		modeLabel.setTextFill(Color.YELLOW);
		Label modeText = new Label(scan.nextLine());
		modeText.setWrapText(true);
		modeText.setFont(popUpContent);
		modeText.setTextFill(Color.YELLOW);
		
		VBox infoHolder = new VBox(10, infoLabel, infoText, rulesLabel, rulesText, modeLabel, modeText);
		infoContent.setCenter(infoHolder);
		infoHolder.setPadding(new Insets(15, 15, 15, 15));
		
		infoPop.getContent().add(infoContent);
		infoContent.setPrefSize(530, 600);
		infoContent.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, null, null)));
		infoPop.setHeight(500);
		infoPop.setWidth(600);
		infoPop.show(BattleshipGUI.getStage());
		scan.close();
	}
}
