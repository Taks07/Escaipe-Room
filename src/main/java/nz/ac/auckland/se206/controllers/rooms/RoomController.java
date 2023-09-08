package nz.ac.auckland.se206.controllers.rooms;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import nz.ac.auckland.se206.GameMediaPlayer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public abstract class RoomController {
  @FXML protected Label actionLabel;
  @FXML protected ImageView arrow1;
  @FXML protected ImageView arrow2;
  @FXML protected ImageView background;

  // TODO This is being initialised every time a room is loaded. It should be initialised once.
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
   * Handles the click event on an object other than the door.
   *
   * @param event the mouse event
   */
  @FXML
  private void clickObject(MouseEvent event) {
    Shape object = (Shape) event.getSource();
    String objectID = object.getId();

    if (GameState.isRiddleAnswerCorrect(objectID)) {
      // Correct object found. Tell user they can click the door to end the game.
      GameState.askGPT(GptPromptEngineering.getDoorCode());
      GameState.isObjectFound = true;
    } else {
      // Not the correct object. Provide some flavour text.
      GameState.sayFlavourText(objectID);
    }
  }

  @FXML
  private void hoverObject(MouseEvent event) {
    ImageView object = (ImageView) event.getSource();
    String objectID = object.getId();
    object.setImage(new Image("/images/objects/" + objectID + "_selected.png"));
    actionLabel.setText(":D");
  }

  @FXML
  private void unhoverObject(MouseEvent event) {
    ImageView object = (ImageView) event.getSource();
    String objectID = object.getId();
    object.setImage(new Image("/images/objects/" + objectID + ".png"));
    actionLabel.setText("");
  }

  @FXML
  private void clickArrow1(MouseEvent event) {
    GameState.prevRoom();
  }

  @FXML
  private void clickArrow2(MouseEvent event) {
    GameState.nextRoom();
  }

  @FXML
  private void hoverArrow1(MouseEvent event) {
    arrow1.setOpacity(1);
    actionLabel.setText("Go to " + GameState.getPrevRoom());
  }

  @FXML
  private void hoverArrow2(MouseEvent event) {
    arrow2.setOpacity(1);
    actionLabel.setText("Go to " + GameState.getNextRoom());
  }

  @FXML
  private void unhoverArrow1(MouseEvent event) {
    actionLabel.setText("");
    arrow1.setOpacity(0.7);
  }

  @FXML
  private void unhoverArrow2(MouseEvent event) {
    actionLabel.setText("");
    arrow2.setOpacity(0.7);
  }

  // TODO This is being initialised every time a room is loaded. It should be initialised once.
  private void animate() {
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
    Thread animateThread = new Thread(animation);
    animateThread.setDaemon(true);
    animateThread.start();
  }
}
