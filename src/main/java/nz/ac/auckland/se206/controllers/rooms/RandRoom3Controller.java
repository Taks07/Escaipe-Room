package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class RandRoom3Controller extends RoomController {

  @FXML private ImageView plantsImage;
  @FXML private ImageView pipeImage;
  @FXML private ImageView shovelImage;
  @FXML private ImageView flowerImage;
  @FXML private ImageView grassImage;

  // TODO: ADD CLICKABLE FOR MINIGAME IN THIS ROOM
  @FXML
  private void clickAlien3(MouseEvent event) {
    GameState.setAlienHead();
    GameState.askGPT(GptPromptEngineering.introduction());
  }
}
