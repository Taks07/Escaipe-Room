package nz.ac.auckland.se206.controllers.minigames;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.controllers.rooms.RoomController;

/**
 * Abstract class for minigame controllers. Contains methods for handling the back arrow click and
 * hovering over objects.
 */
public abstract class MinigameController extends RoomController {
  @FXML private Rectangle fadeRectangle;
  @FXML private ImageView arrow1;

  protected boolean isSolved;

  // protected FadeTransition ft;

  /**
   * Returns user back to room.
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
    fadeOut();
    ft.setOnFinished(
        event2 -> {
          fadeRectangle.setVisible(false);
          GameState.switchRoom(GameState.currRooms.get(GameState.getCurrRoom()));
        });
    ft.play();
  }

  /**
   * Handles the hover event on an object.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverArrow(MouseEvent event) {
    arrow1.setImage(new Image("/images/objects/arrow1_selected.png"));
  }

  /**
   * Handles the unhover event on an object.
   *
   * @param event the mouse event
   */
  @FXML
  private void unhoverArrow(MouseEvent event) {
    arrow1.setImage(new Image("/images/objects/arrow1.png"));
  }

  /** Ends minigame, incrementing part counter and setting the game to solved. */
  protected void endGame() {
    GameState.inMinigame = false;
    isSolved = true;
    GameState.incrementPartsFound();
    GameState.setMinigameSolved();

    // Fade out the room and switch to the next room
    fadeOut();
    ft.setOnFinished(
        event2 -> {
          fadeRectangle.setVisible(false);
          GameState.switchRoom(GameState.currRooms.get(GameState.getCurrRoom()));
        });
    ft.play();
  }
}
