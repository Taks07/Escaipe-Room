package nz.ac.auckland.se206.controllers.minigames;

import java.io.IOException;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import nz.ac.auckland.se206.App;

public class MainRoomMinigameController extends MinigameController {
  @FXML Pane linePane;
  @FXML Slider frequencySlider;
  @FXML Slider amplitudeSlider;

  private final int yOffset = 165;
  private Polyline playerPolyline;
  private double targetAmplitude;
  private double targetFrequency;
  private int round;

  @FXML
  public void initialize() {
    round = 0;

    amplitudeSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!linePane.getChildren().contains(playerPolyline)) {
                return;
              }

              linePane.getChildren().remove(playerPolyline);
              playerPolyline =
                  createPolyline(amplitudeSlider.getValue(), frequencySlider.getValue(), "white");
              linePane.getChildren().add(playerPolyline);
              checkAnswerCorrect();
            });

    frequencySlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!linePane.getChildren().contains(playerPolyline)) {
                return;
              }
              linePane.getChildren().remove(playerPolyline);
              playerPolyline =
                  createPolyline(amplitudeSlider.getValue(), frequencySlider.getValue(), "white");
              linePane.getChildren().add(playerPolyline);
              checkAnswerCorrect();
            });

    playRound();
  }

  /** Play a round of the game */
  public void playRound() {
    linePane.getChildren().removeAll(linePane.getChildren());

    createPlayerPolyline();
    createTargetPolyline();
  }

  /** Create a red target sine wave with random amplitude and frequency */
  public void createTargetPolyline() {
    Random random = new Random();

    // Generate random amplitude and frequency
    targetAmplitude = 135 * random.nextDouble() + 30;
    targetFrequency = 0.065 * random.nextDouble() + 0.635;

    // Create target polyline and display
    Polyline targetPolyline = createPolyline(targetAmplitude, targetFrequency, "red");
    linePane.getChildren().add(targetPolyline);
  }

  /** Create a white player sine wave */
  public void createPlayerPolyline() {
    amplitudeSlider.setValue(100);
    frequencySlider.setValue(0.64);

    // Create player polyline and display
    playerPolyline =
        createPolyline(amplitudeSlider.getValue(), frequencySlider.getValue(), "white");
    linePane.getChildren().add(playerPolyline);
  }

  /**
   * Create a sine wave Polyline node with the given amplitude, frequency and colour
   *
   * @param amplitude amplitude of the sine wave
   * @param frequency frequency of the sine wave
   * @param colour colour of the sine wave (red or green)
   * @return Polyline node
   */
  public Polyline createPolyline(double amplitude, double frequency, String colour) {
    double[] points = new double[128];
    // Calculate each coordinate of the polyline
    for (int i = 0; i < 128; i += 2) {
      points[i] = i * 5;
      points[i + 1] = amplitude * Math.sin(points[i] * frequency) + yOffset;
    }

    // Create polyline
    Polyline polyline = new Polyline(points);
    polyline.setFill(null);

    // Set colour
    switch (colour) {
      case "red":
        polyline.setStroke(javafx.scene.paint.Color.RED);
        break;
      case "green":
        polyline.setStroke(javafx.scene.paint.Color.GREEN);
        break;
      case "white":
        polyline.setStroke(javafx.scene.paint.Color.WHITE);
        break;
    }
    return polyline;
  }

  /** Check if the player's answer is correct (within 1% of the target amplitude and frequency) */
  public void checkAnswerCorrect() {
    boolean answerCorrect =
        Math.abs(amplitudeSlider.getValue() - targetAmplitude) / targetAmplitude < 0.01
            && Math.abs(frequencySlider.getValue() - targetFrequency) / targetFrequency < 0.01;

    if (answerCorrect) {
      // Display correct polyline in green
      linePane.getChildren().removeAll(linePane.getChildren());
      Polyline finishedPolyline = createPolyline(targetAmplitude, targetFrequency, "green");
      linePane.getChildren().add(finishedPolyline);

      round++;

      PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(1));
      if (round == 3) {
        // If 3 rounds have been played, end the game after 1 second
        pause.setOnFinished(
            event -> {
              try {
                App.endGame();
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
      } else {
        // Start a new round after 1 second
        pause.setOnFinished(event -> playRound());
      }

      pause.play();
    }
  }
}
