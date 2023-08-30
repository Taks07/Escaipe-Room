package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameState;

public class BackRoomController extends RoomController {
  @FXML private ImageView mainRoomArrow;

  @FXML
  private void switchToMainRoom() {
    GameState.switchRoom("mainroom");
  }

  @FXML
  private void hoverArrow(MouseEvent event) {
    mainRoomArrow.setOpacity(1);
    actionLabel.setText("Go to front of room");
  }

  @FXML
  private void unhoverArrow(MouseEvent event) {
    mainRoomArrow.setOpacity(0.7);
    actionLabel.setText("");
  }
}
