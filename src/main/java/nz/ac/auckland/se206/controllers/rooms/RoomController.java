package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import nz.ac.auckland.se206.GameMediaPlayer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;

public abstract class RoomController {
  @FXML protected Label actionLabel;
  @FXML protected ImageView arrow1;
  @FXML protected ImageView arrow2;
  @FXML protected ImageView background;
  protected Thread backgroundThread;
  protected Thread flagThread;
  protected boolean visited;

  @FXML
  private void initialize() {
    animate();
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  protected void showDialogBox(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Displays a dialog from the AI system with the given content in the chat box.
   *
   * @param dialog the content of the dialog
   */
  protected void showDialog(String dialog) {
    ChatMessage chat = new ChatMessage("assistant", dialog);
    GameState.showChatMessage(chat);
    GameMediaPlayer.playNotificationSound();
  }

  /**
   * Handles the click event on an object.
   *
   * @param event the mouse event
   * @throws IOException
   */
  @FXML
  protected void clickObject(MouseEvent event) {
    Shape object = (Shape) event.getSource();
    String objectID = object.getId();
    System.out.println("Clicked " + objectID);

    if (GameState.isRiddleAnswerCorrect(objectID)) {
      // Correct object clicked. Increment parts found and set flag.
      System.out.println("Got object");
      GameState.partsFound++;
      GameState.isObjectFound = true;
    } else {
      // Not the correct object. Provide some flavour text.
      GameState.sayFlavourText(objectID);
    }
  }

  /**
   * Handles the hover event on an object.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverObject(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectID = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectID);
    image.setImage(new Image("/images/objects/" + objectID + "_selected.png"));

    actionLabel.setText(":D");
  }

  /**
   * Handles the unhover event on an object.
   *
   * @param event the mouse event
   */
  @FXML
  private void unhoverObject(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectID = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectID);
    image.setImage(new Image("/images/objects/" + objectID + ".png"));

    actionLabel.setText("");
  }

  /**
   * Handles the click event on the minigame object.
   *
   * @param event the mouse event
   */
  @FXML
  private void clickMinigame(MouseEvent event) {
    // Check if minigame has been solved
    if (GameState.getMinigameSolved()) {
      return;
    }

    String fxmlPath = GameState.currRooms.get(GameState.getCurrRoom()) + "minigame";
    GameState.switchRoom(fxmlPath);
  }

  /**
   * Handles the click event on the left arrow.
   *
   * @param event the mouse event
   */
  @FXML
  private void clickArrow1(MouseEvent event) {
    backgroundThread.interrupt();
    if (flagThread != null) {
      flagThread.interrupt();
    }
    GameState.prevRoom();
  }

  /**
   * Handles the click event on the right arrow.
   *
   * @param event the mouse event
   */
  @FXML
  private void clickArrow2(MouseEvent event) {
    backgroundThread.interrupt();
    if (flagThread != null) {
      flagThread.interrupt();
    }
    GameState.nextRoom();
  }

  /**
   * Handles the hover event on the left arrow.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverArrow1(MouseEvent event) {
    arrow1.setOpacity(1);
    actionLabel.setText("Go to " + GameState.getPrevRoom());
  }

  /**
   * Handles the hover event on the right arrow.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverArrow2(MouseEvent event) {
    arrow2.setOpacity(1);
    actionLabel.setText("Go to " + GameState.getNextRoom());
  }

  /**
   * Handles the unhover event on the left arrow.
   *
   * @param event the mouse event
   */
  @FXML
  private void unhoverArrow1(MouseEvent event) {
    actionLabel.setText("");
    arrow1.setOpacity(0.7);
  }

  /**
   * Handles the unhover event on the right arrow.
   *
   * @param event the mouse event
   */
  @FXML
  private void unhoverArrow2(MouseEvent event) {
    actionLabel.setText("");
    arrow2.setOpacity(0.7);
  }

  /**
   * Animates the background.
   *
   * @throws InterruptedException
   */
  protected void animate() {
    Task<Void> animation =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (true) {
              Thread.sleep(500);
              Platform.runLater(
                  () -> {
                    // Animate 1
                    background.setImage(new Image("/images/rooms/background2.png"));
                  });
              Thread.sleep(500);
              Platform.runLater(
                  () -> {
                    // Animate 2
                    background.setImage(new Image("/images/rooms/background1.png"));
                  });
            }
          }
        };
    backgroundThread = new Thread(animation);

    backgroundThread.setDaemon(true);
    backgroundThread.start();
  }
}
