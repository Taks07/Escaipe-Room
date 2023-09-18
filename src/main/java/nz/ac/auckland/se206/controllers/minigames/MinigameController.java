package nz.ac.auckland.se206.controllers.minigames;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;

public abstract class MinigameController {
  @FXML
  public void clickBackArrow(MouseEvent event) {
    GameState.switchRoom(GameState.currRooms.get(GameState.getCurrRoom()));
  }

  @FXML
  private void hoverObject(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectID = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectID);
    image.setImage(new Image("/images/objects/" + objectID + "_selected.png"));
  }

  @FXML
  private void unhoverObject(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectID = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectID);
    image.setImage(new Image("/images/objects/" + objectID + ".png"));
  }

  protected void endGame() {
    GameState.partsFound++;
    GameState.switchRoom(GameState.currRooms.get(GameState.getCurrRoom()));
  }
}
