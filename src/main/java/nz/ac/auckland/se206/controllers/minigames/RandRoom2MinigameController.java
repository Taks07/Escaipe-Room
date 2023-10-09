package nz.ac.auckland.se206.controllers.minigames;

import java.io.IOException;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * Controller for the random oxygen meter minigame in Room 2. The player must click on the pane to
 * move the slider to the right. The objective is to move the slider int between the target
 * positions for 2 seconds each round. The player must complete 5 rounds to win the game.
 */
public class RandRoom2MinigameController extends MinigameController {

  @FXML private Line slider;
  @FXML private Line slider1;
  @FXML private Line slider2;

  // The position of the slider
  private double sliderPosition = 342;

  // The speed of the oxygen meter
  private double oxygenSpeed = 1.5;
  private boolean gameRunning = true;
  private int currentRound = 0;
  private int roundsToWin = 5;
  private Random random = new Random();
  private double targetPosition;
  private double rectangleMinX = 91.0;
  private double rectangleMaxX = 617.0;
  private int start = 0;
  private AnimationTimer gameLoop;

  @FXML private Button button1;
  @FXML private Button button2;
  @FXML private Button button3;
  @FXML private Button button4;
  @FXML private Button button5;

  // Timer variables
  private int timeInTargetRange = 0;
  private boolean inTargetRange = false;

  private final int requiredTimeInTargetRange = 120; // 2 seconds

  // Flags to indicate if the player has passed the target position for the current round in either
  private boolean passedTargetPositive = false;
  private boolean passedTargetNegative = false;

  public void initialize() {
    fadeIn();

    // Assign the gameLoop to the class-level variable
    gameLoop =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            if (gameRunning) {
              updateOxygenMeter();
            }
          }
        };

    // Start the game loop
    gameLoop.start();

    // Start the first round
    startNewRound();
  }

  /**
   * Handles the click event on the pane. Moves the slider to the right by 75 pixels.
   *
   * @param event the mouse event
   * @throws IOException
   */
  @FXML
  private void handlePaneClick(MouseEvent event) {
    // whenever you click on the pane, the slider will move 75 pixels to the right
    if (gameRunning) {
      sliderPosition += 75;
      gameLoop.start();
    }
    start++;
  }

  /**
   * Starts a new round of the game. Generates a random target position for the slider for the
   * current round.
   */
  private void startNewRound() {

    passedTargetPositive = false;
    passedTargetNegative = false;

    // Generate a random target position for the slider1 for the current round
    targetPosition = rectangleMinX + random.nextDouble() * (rectangleMaxX - rectangleMinX - 185);
    currentRound++;
  }

  /**
   * Updates the position of the oxygen meter based on its speed. Checks if the slider is out of the
   * desired range. Checks if the player has passed the target position for the current round in
   * either direction. Checks if the player has won or lost.
   */
  private void updateOxygenMeter() {
    // Update the position of the oxygen meter based on its speed
    if (start > 0) {
      sliderPosition -= oxygenSpeed;
    }

    // Check if the slider is out of the desired range, if so then game over
    if (sliderPosition <= rectangleMinX || sliderPosition >= rectangleMaxX) {
      gameRunning = false;
      sliderPosition = 342;
      setButtonsRed(); // Set buttons to red
      PauseTransition pause = new PauseTransition(Duration.seconds(1));
      pause.setOnFinished(event2 -> resetButtons()); // Reset buttons after 1 second
      pause.play();
      start = 0;
      gameLoop.stop();
      System.out.println("Game over!");
      resetGame(); // Call the resetGame method when the player loses
      timeInTargetRange = 0; // Reset the timer when the player loses
      inTargetRange = false;
    }

    // Check if the player has passed the target position for the current round in either direction
    if (sliderPosition >= targetPosition) {
      passedTargetPositive = true;
      passedTargetNegative = false; // Reset the negative flag
    } else if (sliderPosition <= targetPosition) {
      passedTargetPositive = false; // Reset the positive flag
      passedTargetNegative = true;
    }

    // check if slider is between target sliders
    if (sliderPosition > targetPosition && sliderPosition < targetPosition + 175) {
      inTargetRange = true; // Player is in the target range
      timeInTargetRange += 1.0;
      // once 2 seconds have passed, move onto next round while increasing the button colour by 1
      if (timeInTargetRange >= requiredTimeInTargetRange) {
        startNewRound();
        String buttonId = "button" + (currentRound - 1);
        Button button = getButtonById(buttonId);
        button.setStyle("-fx-background-color: green;");
        timeInTargetRange = 0;
      }
    } else {
      inTargetRange = false;
      timeInTargetRange = 0;
    }

    // Check if the player has won
    if (currentRound > roundsToWin) {
      gameRunning = false;
      System.out.println("You won the game!");
      gameLoop.stop();
      PauseTransition pause = new PauseTransition(Duration.seconds(1));
      pause.setOnFinished(event2 -> endGame());
      pause.play();
    }

    // Update the slider's position
    slider.setLayoutX(sliderPosition);
    slider1.setLayoutX(targetPosition);
    slider2.setLayoutX(targetPosition + 175);
  }

  /** Sets all buttons to red. */
  private void setButtonsRed() {
    button1.setStyle("-fx-background-color: red;");
    button2.setStyle("-fx-background-color: red;");
    button3.setStyle("-fx-background-color: red;");
    button4.setStyle("-fx-background-color: red;");
    button5.setStyle("-fx-background-color: red;");
  }

  /** Resets all buttons to their default colour. */
  private void resetButtons() {
    button1.setStyle("");
    button2.setStyle("");
    button3.setStyle("");
    button4.setStyle("");
    button5.setStyle("");
  }

  /** Returns the button with the given id. */
  private Button getButtonById(String buttonId) {
    // switches between the button ids and returns the button with the given id
    switch (buttonId) {
      case "button1":
        return button1;
      case "button2":
        return button2;
      case "button3":
        return button3;
      case "button4":
        return button4;
      case "button5":
        return button5;
      default:
        return null;
    }
  }

  /** Resets the game to zero wins when the player loses. */
  private void resetGame() {
    currentRound = 0;
    gameRunning = true;
    startNewRound();
  }
}
