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
  @FXML private Button button03;
  @FXML private Button button13;
  @FXML private Button button23;
  @FXML private Button button33;

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
  @FXML private ImageView image03;
  @FXML private ImageView image13;
  @FXML private ImageView image23;
  @FXML private ImageView image33;

  private Button[][] buttons;
  private ImageView[][] imageViews;
  private Button prevButton;
  private PauseTransition pause;
  private int pairs;

  public void initialize() {
    buttons =
        new Button[][] {
          {button00, button10, button20, button30},
          {button01, button11, button21, button31},
          {button02, button12, button22, button32},
          {button03, button13, button23, button33}
        };
    imageViews =
        new ImageView[][] {
          {image00, image10, image20, image30},
          {image01, image11, image21, image31},
          {image02, image12, image22, image32},
          {image03, image13, image23, image33}
        };

    pairs = 0;
    pause = new PauseTransition(Duration.seconds(1));
    randomizeSymbols();
  }

  /** Randomizes the symbols on the buttons. */
  public void randomizeSymbols() {
    // Make sure there is 2 of each symbol on each button
    ArrayList<String> symbols =
        new ArrayList<String>(
            Arrays.asList(
                "\u0E04", "\u0E52", "\u03C2", "\u0E54", "\u0454", "\u0166", "\uFEEE", "\u0452",
                "\u0E04", "\u0E52", "\u03C2", "\u0E54", "\u0454", "\u0166", "\uFEEE", "\u0452"));
    Random rand = new Random();

    // Loop through all buttons
    for (int i = 0; i < 4; i++) {
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

  /** Handles click on button */
  @FXML
  private void clickButton(ActionEvent event) {
    Button button = (Button) event.getSource();

    // If button is already visible, don't do anything
    if (button.getOpacity() == 1) {
      return;
    }

    // If there is a pause, don't do anything
    if (pause.statusProperty().get() == javafx.animation.Animation.Status.RUNNING) {
      return;
    }

    if (prevButton == null) {
      // If button has not been clicked yet, simply show text
      button.setOpacity(1);
      prevButton = button;
    } else {
      // Button has already been clicked, so check if it matches the previous button
      button.setOpacity(1);

      if (button.getText().equals(prevButton.getText())) {
        // If it matches, keep both buttons visible
        prevButton = null;

        pairs++;

        // If all pairs have been found, end the game
        if (pairs == 8) {
          pause = new PauseTransition(Duration.seconds(1));
          pause.setOnFinished(
              event2 -> {
                endGame();
              });
          pause.play();
        }
      } else {
        // If it doesn't match, hide both buttons after a second
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
