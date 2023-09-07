package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Controller class for the room view. */
public class MainRoomController extends RoomController {

  @FXML private ImageView rocketImage;

  // TODO: Change to rocket instead of door?
  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  private void clickDoor(MouseEvent event) throws IOException {
    // Check if the door code has been entered
    if (GameState.isDoorCodeEntered) {
      App.setRoot("results");
      return;
    }

    // Check if the riddle has been resolved and the object has been found
    if (GameState.isRiddleResolved && GameState.isObjectFound) {
      showDialog("Enter the code at the keypad to unlock the door.");
      return;
    }

    // Check if the riddle has been resolved but the object has not been found
    if (GameState.isRiddleResolved && !GameState.isObjectFound) {
      showDialog("You must find the object before you can get the code.");
      return;
    }

    // Check if the riddle has been asked
    if (GameState.currRiddle != null) {
      showDialog("Here is the riddle again:\n" + GameState.currRiddle);
    } else {
      GameState.askGPT(
          GptPromptEngineering.getRiddleWithGivenWord(GameState.getCurrRiddleAnswer()));
    }
  }
}
