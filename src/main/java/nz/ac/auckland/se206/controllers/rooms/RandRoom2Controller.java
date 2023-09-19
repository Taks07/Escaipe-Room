package nz.ac.auckland.se206.controllers.rooms;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class RandRoom2Controller extends RoomController {

  @FXML private ImageView oxygenImage;
  @FXML private ImageView flagImage;
  @FXML private ImageView cubeImage;
  @FXML private ImageView carImage;
  @FXML private ImageView earthImage;

  @FXML
  private void initialize() {
    animateFlag();
    animate();
  }

  private void animateFlag() {
    Task<Void> animation =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (true) {
              Thread.sleep(550);
              Platform.runLater(
                  () -> {
                    // Animate 1
                    flagImage.setImage(new Image("/images/objects/flag2.png"));
                  });
              Thread.sleep(550);
              Platform.runLater(
                  () -> {
                    // Animate 2
                    flagImage.setImage(new Image("/images/objects/flag1.png"));
                  });
            }
          }
        };
    flagThread = new Thread(animation);

    flagThread.setDaemon(true);
    flagThread.start();
  }

  @FXML
  private void clickAlien2(MouseEvent event) {
    GameState.setAlienHead();
    GameState.askGPT(GptPromptEngineering.introduction());
  }

  @FXML
  private void hoverCrater(MouseEvent event) {
    if (GameState.getMinigameSolved()) {
      actionLabel.setText("Part already found!");
      return;
    }
    actionLabel.setText("Search the crater for parts");
  }
}
