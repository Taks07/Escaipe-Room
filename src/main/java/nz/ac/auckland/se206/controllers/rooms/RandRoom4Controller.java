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

  @FXML
  private void hoverCave(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectId = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectId);
    image.setImage(new Image("/images/objects/" + objectId + "_selected.png"));
    if (GameState.getMinigameSolved()) {
      actionLabel.setText("Part already found!");
      return;
    }
    actionLabel.setText("Attempt cave puzzle for part");
  }
}
