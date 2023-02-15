package com.game.fxpowerballgame;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;

public class PowerBallController {
	@FXML
	private Label gameResults_lb, errorLabel;
	@FXML
	private JFXButton play_btn;
	@FXML
	private JFXCheckBox autoPlay_cb, manualPlay_cb;
	@FXML
	private TextField playBalance_tf, totalWins_tf;
	@FXML
	private HBox playerGuesses_hb, powerBall_hb;

	// instance variable
	Set <Integer> playerSet;
	Set <Integer>powerballSet;

	AtomicInteger countWins = new AtomicInteger(0);



	/* *************** Class Methods **********************/

	//random ball generator
	@FXML public void randomNumberGenerator(HBox tempBox, Set<Integer>tempSet){
		RandomGenerator randomNumbers = RandomGenerator.getDefault();
		tempBox.getChildren().forEach(node -> {
			var powerSource = randomNumbers.nextInt(0, 99);
			((TextField)node).setText(String.valueOf(powerSource));
			tempSet.add(powerSource);
		});
	}


	@FXML public int countNumberOfWins(int tempWins){
	    var totals = Integer.parseInt(totalWins_tf.getText());
	   return  countWins.getAndAdd(totals);
	}
	

	//autoPlay mode
	@FXML public void autoPlayFunction(){
		// initializing both hashsets
		powerballSet = new HashSet<>();
		playerSet = new HashSet<>();

		//assign random numbers to game text-fields via nodes
		randomNumberGenerator(powerBall_hb,powerballSet);
		randomNumberGenerator(playerGuesses_hb,playerSet);

		//check whether sets contain same elements
		var matchingElements = playerSet.stream().filter(powerballSet::contains).count();
		createPlayerPayOutChart((int)matchingElements);
	}


	@FXML public void manualPlayMode(){
		powerballSet = new HashSet<>();
		playerSet = new HashSet<>();

		playerGuesses_hb.getChildren().forEach(node -> playerSet.add(Integer.valueOf(((TextField)node).getText())));

		randomNumberGenerator(powerBall_hb, powerballSet);
		var matchingElements = powerballSet.stream().filter(playerSet::contains).count();
		createPlayerPayOutChart((int)matchingElements);
	}


	//game prompt in list form
	public List<String>gamePrompts(){
		return List.of(
			"You are out of money. Game Over!",
			"You Lose -$10.00!",
			"You won $10",
			"You won $20",
			"You won $30",
			"You won $40",
			"You won the jackpot of $100.00",
			"Error 1 to 2 digits only allowed!",
			"All fields required to play the game!");
	}


	// game payout chart  uses java's new switch expression
	//JDK 14 and above required to run this code.
	@FXML public void createPlayerPayOutChart(int matchingInts){
	   var currentBalance =Integer.parseInt( playBalance_tf.getText());
	    switch(matchingInts){
			case 0 -> {
				var gameLost = currentBalance - 10;
				if( currentBalance > 0){
					playBalance_tf.setText(String.valueOf(gameLost));
					gameResults_lb.setText(gamePrompts().get(1));

				} else
					gameResults_lb.setText(gamePrompts().get(0));
			}
			case 1 ->{
				var wins =  currentBalance + 10;
				playBalance_tf.setText(String.valueOf(wins));
				totalWins_tf.setText(String.valueOf(countWins.accumulateAndGet(matchingInts
					,Integer::sum)));
				gameResults_lb.setText(gamePrompts().get(2));
			}
			case 2 -> {
				var wins =  currentBalance + 20;
				playBalance_tf.setText(String.valueOf(wins));
				totalWins_tf.setText(String.valueOf(countWins.accumulateAndGet(matchingInts
					,Integer::sum)));
				gameResults_lb.setText(gamePrompts().get(3));
			}
			case 3 -> {
				var wins = currentBalance + 30;
				playBalance_tf.setText(String.valueOf(wins));
				totalWins_tf.setText(String.valueOf(countWins.accumulateAndGet(matchingInts
					,Integer::sum)));
				gameResults_lb.setText(gamePrompts().get(4));
			}
			case 4 -> {
				var wins =  currentBalance + 40;
				playBalance_tf.setText(String.valueOf(wins));
				totalWins_tf.setText( String.valueOf(countNumberOfWins(matchingInts)));
				gameResults_lb.setText(gamePrompts().get(5));
			}
			case 5 -> {
				var wins  = currentBalance + 100;
				playBalance_tf.setText(String.valueOf(wins));
				totalWins_tf.setText( String.valueOf(countNumberOfWins(matchingInts)));
				gameResults_lb.setText(gamePrompts().get(6));
			}
	    }

	}

	// group checkboxes
	@FXML public void groupCheckboxes(){
		 if(autoPlay_cb.isSelected())
			 manualPlay_cb.setSelected(false);
		 else
			 autoPlay_cb.setSelected(false);
		 limitNumberOfDigits();

	 }


	 /*  Validation methods  */
	
	@FXML public void checkPlayerBalance(){
		if(Integer.parseInt(playBalance_tf.getText())==0){
			play_btn.setDisable(true);
			gameResults_lb.setText(gamePrompts().get(0));

		}
	}

   //method to check for empty player by accessing
	//children nodes of playerGuesses_hb.
	//the text-fields border color is changed to red with
	//solid beige background with transparent prompt text.
    @FXML public void checkForEmptyPlayerFields(){
	         playerGuesses_hb.getChildren().forEach(node -> {
			((TextField)node).textProperty().addListener((observable, oldValue, newValue) -> {
				if(newValue.isEmpty())
					node.setStyle("-fx-border: none none solid red; -fx-background-color:beige; -fx-prompt-text-fill:rgba(248,176,130,0.5)");
				 ((TextField) node).setPromptText("?");
					powerBall_hb.setVisible(false);
					errorLabel.setText(gamePrompts().get(8));
					play_btn.isDisabled();
			});
		});
    }


	//player manual play will only permit 1 to 2 digits
	@FXML public void limitNumberOfDigits(){
		var requiredInput = "\\d{1,2}$";
		playerGuesses_hb.getChildren().forEach(node -> {
			((TextField)node).textProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue.matches(requiredInput))
					((TextField)node).setText(oldValue);
				((TextField) node).setText(" ");
			});
		});
	}


	//play button method handles two play options
	//autoplay and manual play
	 @FXML public void playButtonFunction() {
		 if (autoPlay_cb.isSelected()) {
			 manualPlay_cb.setSelected(false);
			 autoPlayFunction();
			 checkPlayerBalance();
			 play_btn.setText("Play Again!");
		 }
		 if (manualPlay_cb.isSelected())  {
			 autoPlay_cb.setSelected(false);
			 checkPlayerBalance();
			 manualPlayMode();
			 checkForEmptyPlayerFields();
			 play_btn.setText("Play Again!");

		 }
	 }


	 @FXML public void activateTextFieldValidation(){
		if(manualPlay_cb.isSelected() && play_btn.isPressed())
			checkForEmptyPlayerFields();
	 }


	 //rest powerball fields
	 @FXML public void resetPowerballFields(){
		powerBall_hb.getChildren().forEach(node ->
			((TextField)node).setText(" "));
	 }

	 //reset player fields
	 @FXML public void resetPlayerFields(ActionEvent ae){
		 playerGuesses_hb.getChildren().forEach(node ->
			 ((TextField)node).setText(" "));
	 }

	 //reset labels and singular fields
	 @FXML public void resetLabelsAndSingularFields(){
		 gameResults_lb.setText(" ");
		 playBalance_tf.setText("100");
		 totalWins_tf.setText(" ");
		
	 }

	 //resets all  fields for new game
	 @FXML public void replayGame(ActionEvent ae){
		 resetPlayerFields(ae);
		 resetPowerballFields();
	 	resetLabelsAndSingularFields();
		 autoPlay_cb.setSelected(false);
		 manualPlay_cb.setSelected(false);
	 }

	  //exits game
	 @FXML public void quitGameFunction(ActionEvent ae){  //works
		Platform.exit();

	 }

}



