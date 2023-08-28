package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import nz.ac.auckland.se206.GameMediaPlayer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public abstract class RoomController {
  @FXML protected Label actionLabel;

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
    actionLabel.setText("Point at object");
  }

  @FXML
  private void unhoverObject(MouseEvent event) {
    actionLabel.setText("");
  }
}
