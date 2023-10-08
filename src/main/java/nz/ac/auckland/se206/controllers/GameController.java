package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;

/** Controller class for the main game view. */
public class GameController {
  /**
   * Loads the FXML file with the given name and return its loader.
   *
   * @param fxml the name of the FXML file to load
   * @return the FXMLLoader used to load the FXML file
   * @throws IOException if there is an error loading the FXML file
   */
  private static FXMLLoader loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  @FXML private BorderPane mainBorderPane;
  @FXML private Label timerLabel;
  @FXML private Button muteButton;
  @FXML private Button flavourTextButton;
  @FXML private ImageView mute;
  @FXML private ImageView unmute;

  /** Code that is run when first starting game */
  @FXML
  public void initialize() {
    setMuteButtonText();
    setFlavourTextButtonText();
    try {
      mainBorderPane.setCenter(loadFxml("mainroom").load());
      FXMLLoader chatFxmlLoader = loadFxml("chat");
      mainBorderPane.setBottom(chatFxmlLoader.load());
      // Get the controller of the chat pane and pass it to GameState
      GameState.setChatController(chatFxmlLoader.getController());
      timerLabel.textProperty().bind(GameTimer.timerLabel);
      GameState.setGameController(this);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onClickMute(ActionEvent event) {
    GameState.toggleMute();
    setMuteButtonText();
  }

  @FXML
  private void onClickFlavourText(ActionEvent event) {
    GameState.toggleFlavourText();
    setFlavourTextButtonText();
  }

  private void setMuteButtonText() {
    if (GameState.isMuted == true) {
      mute.setVisible(false);
      unmute.setVisible(true);
    } else {
      mute.setVisible(true);
      unmute.setVisible(false);
    }
  }

  /** Sets the text of the flavour text button according to GameState.isFlavourTextEnabled */
  private void setFlavourTextButtonText() {
    if (GameState.isFlavourTextEnabled == true) {
      flavourTextButton.setText("Disable object flavour text");
    } else {
      flavourTextButton.setText("Enable object flavour text");
    }
  }

  @FXML
  protected void hoverMuteButton() {
    if (GameState.isMuted == true) {

      unmute.setImage(new Image("/images/objects/unmute_selected.png"));
    } else {

      mute.setImage(new Image("/images/objects/mute_selected.png"));
    }
  }

  @FXML
  protected void unhoverMuteButton() {
    if (GameState.isMuted == true) {
      unmute.setImage(new Image("/images/objects/unmute.png"));
    } else {

      mute.setImage(new Image("/images/objects/mute.png"));
    }
  }

  public void switchRoom(String fxml) {
    try {
      mainBorderPane.setCenter(loadFxml(fxml).load());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
