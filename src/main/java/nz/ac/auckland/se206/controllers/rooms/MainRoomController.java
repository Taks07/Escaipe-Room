package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Controller class for the room view. */
public class MainRoomController extends RoomController {

  @FXML private ImageView rocketImage;

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  private void clickAlien(MouseEvent event) throws IOException {
    GameState.setAlienHead();
    // Check if the riddle has been asked
    if (GameState.currRiddle != null) {
      showDialog("Here is the riddle again:\n" + GameState.currRiddle);
    } else {
      GameState.askGpt(
          GptPromptEngineering.getRiddleWithGivenWord(GameState.getCurrRiddleAnswer()));
    }
  }

  @FXML
  protected void hoverAlien(MouseEvent event) {
    actionLabel.setText("Ask for riddle");
  }

  @FXML
  private void hoverRocket(MouseEvent event) {
    hoverObject(event);
    if (GameState.partsFound == 3) {
      actionLabel.setText("Begin synchronising communication systems for launch");
    } else {
      actionLabel.setText("Look at rocket");
    }
  }

  @FXML
  private void clickRocket(MouseEvent event) throws IOException {
    if (GameState.partsFound == 3) {
      clickMinigame(event);
    } else {
      clickObject(event);
    }
  }
}
