package nz.ac.auckland.se206.controllers.minigames;

import java.io.IOException;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * Controller for the random oxygen meter minigame in Room 2. The player must click on the pane to
 * move the slider to the right. The objective is to move the slider to the target position for each
 * round. The player must complete 5 rounds to win the game.
 */
public class RandRoom2MinigameController extends MinigameController {

  @FXML private Line slider;
  @FXML private Line slider1;

  // The position of the slider
  private double sliderPosition = 319;

  // The speed of the oxygen meter
  private double oxygenSpeed = 2.5;
  private boolean gameRunning = true;
  private int currentRound = 0;
  private int roundsToWin = 5;
  private Random random = new Random();
  private double targetPosition;
  private double rectangleMinX = 62.0;
  private double rectangleMaxX = 560.0;
  private int start = 0;
  private AnimationTimer gameLoop;

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
   * Handles the click event on the pane. Moves the slider to the right by 50 pixels.
   *
   * @param event the mouse event
   * @throws IOException
   */
  @FXML
  private void handlePaneClick(MouseEvent event) {
    // whenever you click on the pane, the slider will move 50 pixels to the right
    if (gameRunning) {
      sliderPosition += 50;
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
    targetPosition =
        rectangleMinX + 10 + random.nextDouble() * (rectangleMaxX - rectangleMinX - 20);
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
      start = 0;
      gameLoop.stop();
      System.out.println("Game over!");
      resetGame(); // Call the resetGame method when the player loses
    }

    // Check if the player has passed the target position for the current round in either direction
    if (sliderPosition >= targetPosition) {
      passedTargetPositive = true;
    } else if (sliderPosition <= targetPosition) {
      passedTargetNegative = true;
    }

    // Check if both flags are set, indicating that the player has passed the target in both
    // directions
    if (passedTargetPositive && passedTargetNegative) {
      startNewRound();
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
  }

  /** Resets the game to zero wins when the player loses. */
  private void resetGame() {
    currentRound = 0;
    gameRunning = true;
    startNewRound();
    slider.setLayoutX(319);
  }
}
