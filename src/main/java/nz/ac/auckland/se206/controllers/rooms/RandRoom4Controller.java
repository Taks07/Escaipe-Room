package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class RandRoom4Controller extends RoomController {

  @FXML private ImageView caveImage;
  @FXML private ImageView spikes1Image;
  @FXML private ImageView spikes2Image;

  @FXML
  private void clickAlien4(MouseEvent event) {
    GameState.setAlienHead();
    GameState.askGpt(GptPromptEngineering.getIntroduction());
  }

  /**
   * Hovering over the cave image will change the image to the selected version and display a
   * message in the action label.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverCave(MouseEvent event) {

    // If the minigame is solved, don't allow the player to leave manually
    Rectangle object = (Rectangle) event.getSource();
    String objectId = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectId);
    image.setImage(new Image("/images/objects/" + objectId + "_selected.png"));
    if (GameState.getMinigameSolved()) {
      // If the minigame is solved, tell the player the part is already found
      actionLabel.setText("Part already found!");
      return;
    }
    // Otherwise, tell the player to attempt the cave puzzle for the part
    actionLabel.setText("Attempt cave puzzle for part");
  }
}
