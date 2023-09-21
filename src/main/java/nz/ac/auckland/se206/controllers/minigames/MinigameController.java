package nz.ac.auckland.se206.controllers.minigames;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;

public abstract class MinigameController {
  protected boolean isSolved;

  /**
   * Returns user back to room
   *
   * @param event mouse event
   */
  @FXML
  public void handleBackArrowClick(MouseEvent event) {
    // If the minigame is solved, don't allow the player to leave manually
    if (isSolved) {
      return;
    }

    GameState.inMinigame = false;
    GameState.switchRoom(GameState.currRooms.get(GameState.getCurrRoom()));
  }

  /**
   * Handles the hover event on an object.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverObject(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectId = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectId);
    image.setImage(new Image("/images/objects/" + objectId + "_selected.png"));
  }

  /**
   * Handles the unhover event on an object.
   *
   * @param event the mouse event
   */
  @FXML
  private void unhoverObject(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectId = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectId);
    image.setImage(new Image("/images/objects/" + objectId + ".png"));
  }

  /** Ends minigame, incrementing part counter and setting the game to solved */
  protected void endGame() {
    GameState.inMinigame = false;
    isSolved = true;
    GameState.incrementPartsFound();
    GameState.setMinigameSolved();
    GameState.switchRoom(GameState.currRooms.get(GameState.getCurrRoom()));
  }
}
