package nz.ac.auckland.se206.controllers.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/** Controller for the random symbol matching minigame in Room 4. */
public class RandRoom4MinigameController extends MinigameController {

  @FXML private Button button00;
  @FXML private Button button10;
  @FXML private Button button20;
  @FXML private Button button30;
  @FXML private Button button01;
  @FXML private Button button11;
  @FXML private Button button21;
  @FXML private Button button31;
  @FXML private Button button02;
  @FXML private Button button12;
  @FXML private Button button22;
  @FXML private Button button32;

  @FXML private ImageView image00;
  @FXML private ImageView image10;
  @FXML private ImageView image20;
  @FXML private ImageView image30;
  @FXML private ImageView image01;
  @FXML private ImageView image11;
  @FXML private ImageView image21;
  @FXML private ImageView image31;
  @FXML private ImageView image02;
  @FXML private ImageView image12;
  @FXML private ImageView image22;
  @FXML private ImageView image32;

  private Button[][] buttons;
  private Button prevButton;
  private PauseTransition pause;
  private int pairs;

  /** Initializes the controller, sets initial values, and randomly assigns symbols to buttons. */
  public void initialize() {
    isSolved = false;

    buttons =
        new Button[][] {
          {button00, button10, button20, button30},
          {button01, button11, button21, button31},
          {button02, button12, button22, button32}
        };
    pairs = 0;
    pause = new PauseTransition(Duration.seconds(1));
    randomizeSymbols();
  }

  /** Randomizes the symbols on the buttons. */
  public void randomizeSymbols() {
    // Make sure there are 2 of each symbol on each button
    ArrayList<String> symbols =
        new ArrayList<String>(
            Arrays.asList(
                "\u0E04", "\u0E52", "\u03C2", "\u0E54", "\u0454", "\u0166", "\u0E04", "\u0E52",
                "\u03C2", "\u0E54", "\u0454", "\u0166"));
    Random rand = new Random();

    // Loop through all buttons
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 4; j++) {
        // Get a random symbol
        int randomIndex = rand.nextInt(symbols.size());
        String symbol = symbols.get(randomIndex);

        // Set the symbol on the button
        buttons[i][j].setText(symbol);

        // Remove the symbol from the list of symbols
        symbols.remove(randomIndex);
      }
    }
  }

  /**
   * Handles click on a button to reveal symbols and match pairs.
   *
   * @param event The action event triggered by clicking a button.
   */
  @FXML
  private void onButtonClick(ActionEvent event) {
    Button button = (Button) event.getSource();

    // If the button is already visible, do nothing
    if (button.getOpacity() == 1) {
      return;
    }

    // If there is an ongoing pause, do nothing
    if (pause.statusProperty().get() == javafx.animation.Animation.Status.RUNNING) {
      return;
    }

    if (prevButton == null) {
      // If no button has been clicked yet, reveal the symbol
      button.setOpacity(1);
      prevButton = button;
    } else {
      // A button has already been clicked, check if it matches the previous button
      button.setOpacity(1);

      if (button.getText().equals(prevButton.getText())) {
        // If they match, keep both buttons visible
        prevButton = null;

        pairs++;

        // If all pairs have been found, end the game
        if (pairs == 6) {
          pause = new PauseTransition(Duration.seconds(1));
          pause.setOnFinished(
              event2 -> {
                endGame();
              });
          pause.play();
        }
      } else {
        // If they don't match, hide both buttons after a second
        pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(
            event2 -> {
              button.setOpacity(0);
              prevButton.setOpacity(0);
              prevButton = null;
            });
        pause.play();
      }
    }
  }
}
