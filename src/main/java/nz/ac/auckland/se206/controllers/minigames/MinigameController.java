package nz.ac.auckland.se206.controllers.minigames;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;

public abstract class MinigameController {
  @FXML private Rectangle fadeRectangle;

  protected boolean isSolved;
  protected FadeTransition ft;

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

    // Fade out the room and switch to the next room
    fadeOut();
    ft.setOnFinished(
        event2 -> {
          fadeRectangle.setVisible(false);
          GameState.switchRoom(GameState.currRooms.get(GameState.getCurrRoom()));
        });
    ft.play();
  }

  /** Fades in the room. */
  protected void fadeIn() {
    fadeRectangle.setOpacity(1.0);
    fadeRectangle.setVisible(true);
    ft = new FadeTransition(Duration.millis(300), fadeRectangle);
    ft.setFromValue(1.0);
    ft.setToValue(0.0);

    // Makes sure that the fade rectangle is not visible after the fade in
    ft.setOnFinished(
        event -> {
          fadeRectangle.setVisible(false);
        });

    ft.play();
  }

  /**
   * Sets up the fade out for the room. Need to manually play ft after setting destination room in
   * setOnFinished
   */
  protected void fadeOut() {
    ft = new FadeTransition(Duration.millis(300), fadeRectangle);
    ft.setFromValue(0.0);
    ft.setToValue(1.0);
    fadeRectangle.setVisible(true);
  }
}
