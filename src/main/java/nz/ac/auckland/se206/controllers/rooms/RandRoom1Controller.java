package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    GameState.askGpt(GptPromptEngineering.introduction());
  }

  @FXML
  private void hoverUfo(MouseEvent event) {
    if (GameState.getMinigameSolved()) {
      actionLabel.setText("Part already found!");
      return;
    }
    actionLabel.setText("Try to unlock UFO storage for part");
  }
}
