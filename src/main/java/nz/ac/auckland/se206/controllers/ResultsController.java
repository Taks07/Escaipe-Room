package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;

/** Controller class for the results view. */
public class ResultsController extends TitleController {

  @FXML private ImageView results;
  @FXML private ImageView mainbackground;
  @FXML private ImageView flyingrocket;
  @FXML private ImageView exit;
  @FXML private ImageView mainmenu;

  /** Initializes the results view and displays the appropriate result text and animations. */
  @FXML
  public void initialize() {
    if (GameState.gameWon) {
      results.setImage(new Image("images/title_screen/wintext.png"));
      flyingrocket.setVisible(true);
      animateRocket();
    } else {
      results.setImage(new Image("images/title_screen/losetext.png"));
    }
    animate();

    // Stop any ongoing game-related timers and audio
    GameTimer.stopTts();
    GameTimer.stopTimeline();
  }

  /** Switches to the title screen when the "Exit" button is clicked. */
  @FXML
  private void switchToTitle() {
    backgroundThread.interrupt();

    // Stop any ongoing game-related timers and audio
    if (GameState.gameWon) {
      rocketThread.interrupt();
    }

    // set the game state to the title screen
    try {
      App.setRoot("title");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Switches to the main screen when the "Main Menu" button is clicked. */
  @FXML
  private void switchToMainScreen() {
    backgroundThread.interrupt();

    // Stop any ongoing game-related timers and audio
    if (GameState.gameWon) {
      rocketThread.interrupt();
    }

    // set the game state to the mainscreen
    try {
      App.setRoot("mainscreen");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
