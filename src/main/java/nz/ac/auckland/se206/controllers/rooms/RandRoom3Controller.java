package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/**
 * Controller for the third random room. This room contains the plant, pipe, shovel, flower, and
 * grass objects. It holds the teeth minigame for the plant object.
 */
public class RandRoom3Controller extends RoomController {

  @FXML private ImageView plantsImage;
  @FXML private ImageView pipeImage;
  @FXML private ImageView shovelImage;
  @FXML private ImageView flowerImage;
  @FXML private ImageView grassImage;

  @FXML
  private void clickAlien3(MouseEvent event) {
    GameState.setAlienHead();
    GameState.askGpt(GptPromptEngineering.getIntroduction());
  }

  /**
   * Hovering over the plant image will change the image to the selected version and display a
   * message in the action label.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverPlant(MouseEvent event) {

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

    // Otherwise, tell the player to search for parts in the plant's mouth
    actionLabel.setText("Search for parts in plant's mouth");
  }
}
