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
  @FXML private ImageView flavourTextImage;

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
    String name = (GameState.isMuted) ? "unmute" : "mute";
    setButtonImage(name, mute);
  }

  /** Sets the text of the flavour text button according to GameState.isFlavourTextEnabled */
  private void setFlavourTextButtonText() {
    String name = (GameState.isFlavourTextEnabled) ? "disable_flavour_text" : "enable_flavour_text";
    setButtonImage(name, flavourTextImage);
  }

  @FXML
  protected void hoverFlavourTextButton() {
    String name =
        (GameState.isFlavourTextEnabled)
            ? "disable_flavour_text_selected"
            : "enable_flavour_text_selected";
    setButtonImage(name, flavourTextImage);
  }

  @FXML
  protected void unhoverFlavourTextButton() {
    String name = (GameState.isFlavourTextEnabled) ? "disable_flavour_text" : "enable_flavour_text";
    setButtonImage(name, flavourTextImage);
  }

  @FXML
  protected void hoverMuteButton() {
    String name = (GameState.isMuted) ? "unmute_selected" : "mute_selected";
    setButtonImage(name, mute);
  }

  @FXML
  protected void unhoverMuteButton() {
    String name = (GameState.isMuted) ? "unmute" : "mute";
    setButtonImage(name, mute);
  }

  protected void setButtonImage(String name, ImageView image) {
    image.setImage(new Image("/images/objects/" + name + ".png"));
  }

  /**
   * Switches the room to the room with the given FXML file name.
   *
   * @param fxml the name of the FXML file to load
   */
  public void switchRoom(String fxml) {
    try {
      mainBorderPane.setCenter(loadFxml(fxml).load());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
