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

  /** Code that is run when first starting game */
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

    GameTimer.stopTts();
    GameTimer.stopTimeline();
  }

  @FXML
  private void switchToTitle() {
    backgroundThread.interrupt();
    if (GameState.gameWon) {
      rocketThread.interrupt();
    }
    try {
      App.setRoot("title");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void switchToMainScreen() {
    backgroundThread.interrupt();
    if (GameState.gameWon) {
      rocketThread.interrupt();
    }
    try {
      App.setRoot("mainscreen");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
