package nz.ac.auckland.se206.controllers.rooms;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
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
    GameState.askGpt(GptPromptEngineering.getIntroduction());
  }

  @FXML
  private void hoverOxygen(MouseEvent event) {
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

    // Otherwise, tell the player to search for parts in the oxygen canister
    actionLabel.setText("Stabilise the Oxygen Canister to find a part!");
  }
}
