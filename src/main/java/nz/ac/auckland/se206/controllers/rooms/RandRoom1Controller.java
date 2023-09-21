package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class RandRoom1Controller extends RoomController {

  @FXML private ImageView helmetImage;
  @FXML private ImageView panelImage;
  @FXML private ImageView rocksImage;
  @FXML private ImageView tireImage;
  @FXML private ImageView bootImage;
  @FXML private ImageView ufoImage;

  @FXML
  private void clickAlien1(MouseEvent event) {
    GameState.setAlienHead();
    GameState.askGpt(GptPromptEngineering.getIntroduction());
  }

  /**
   * Hovering over the helmet image will change the image to the selected version and display a
   * message in the action label.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverUfo(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectId = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectId);
    image.setImage(new Image("/images/objects/" + objectId + "_selected.png"));
    if (GameState.getMinigameSolved()) {
      actionLabel.setText("Part already found!");
      return;
    }
    actionLabel.setText("Try to unlock UFO storage for part");
  }
}
