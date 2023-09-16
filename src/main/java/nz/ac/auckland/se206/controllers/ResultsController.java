package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;

/** Controller class for the results view. */
public class ResultsController extends TitleController {
  @FXML private Label resultsLabel;
  @FXML private Label resultsExplanationLabel;
  @FXML private ImageView results;
  @FXML private ImageView mainbackground;
  @FXML private ImageView flyingrocket;
  @FXML private ImageView exit;
  @FXML private ImageView mainmenu;

  /** Code that is run when first starting game */
  @FXML
  public void initialize() {
    // backgroundThread.interrupt();
    animate();
    if (flyingrocket != null) {
      animateRocket();
    }
    GameTimer.stopTts();
    GameTimer.stopTimeline();
    resultsLabel.setText("You " + GameState.getResults());
    resultsExplanationLabel.setText(GameState.getResultsExplanation());
  }

  @FXML
  private void switchToTitle() {
    backgroundThread.interrupt();
    if (flyingrocket != null) {
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
    if (flyingrocket != null) {
      rocketThread.interrupt();
    }
    try {
      App.setRoot("mainscreen");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
