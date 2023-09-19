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
  @FXML private ImageView alienImage;

  @FXML
  private void initialize() {
    animate();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  private void clickAlien(MouseEvent event) throws IOException {
    // Check if the riddle has been asked
    if (GameState.currRiddle != null) {
      showDialog("Here is the riddle again:\n" + GameState.currRiddle);
    } else {
      GameState.askGPT(
          GptPromptEngineering.getRiddleWithGivenWord(GameState.getCurrRiddleAnswer()));
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
