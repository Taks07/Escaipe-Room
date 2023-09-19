package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class RandRoom4Controller extends RoomController {

  @FXML private ImageView caveImage;
  @FXML private ImageView spikes1Image;
  @FXML private ImageView spikes2Image;

  @FXML
  private void clickAlien4(MouseEvent event) {
    GameState.setAlienHead();
    GameState.askGPT(GptPromptEngineering.introduction());
  }

  @FXML
  private void hoverCave(MouseEvent event) {
    if (GameState.getMinigameSolved()) {
      actionLabel.setText("Part already found!");
      return;
    }
    actionLabel.setText("Attempt cave puzzle for part");
  }
}
