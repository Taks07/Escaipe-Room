package nz.ac.auckland.se206.controllers.rooms;

import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;

public class KeypadController {
  @FXML private Label actionLabel;
  @FXML private Label keypadLabel;
  @FXML private ImageView mainRoomArrow;

  private final Map<String, String> keypadMap =
      Map.ofEntries(
          Map.entry("one", "1"),
          Map.entry("two", "2"),
          Map.entry("three", "3"),
          Map.entry("four", "4"),
          Map.entry("five", "5"),
          Map.entry("six", "6"),
          Map.entry("seven", "7"),
          Map.entry("eight", "8"),
          Map.entry("nine", "9"),
          Map.entry("zero", "0"));

  /**
   * Handles the click event on a number button.
   *
   * @param event the mouse event
   */
  @FXML
  private void clickNumberButton(MouseEvent event) {
    String currentText = keypadLabel.getText();

    // Max number of digits already entered
    if (countOccurences(currentText, '_') == 0) {
      return;
    }

    Rectangle object = (Rectangle) event.getSource();
    String objectID = object.getId();

    // Replace first occurence of underscore with the number on the button
    keypadLabel.setText(currentText.replaceFirst("_", keypadMap.get(objectID)));
  }

  @FXML
  private void clickEnterButton(MouseEvent event) {
    /*if (GameState.isDoorCodeEntered) {
      return;
    }*/

    String currentCode = keypadLabel.getText().replaceAll(" ", "");

    // Check if code is correct
    /*if (currentCode.equals(GameState.doorCode)) {
      GameState.isDoorCodeEntered = true;
      keypadLabel.setText("Correct!");
      GameState.askGPT(GptPromptEngineering.getDoorUnlocked());
    } else {
      // Incorrect code. Reset keypad label after 2 second
      keypadLabel.setText("Incorrect!");

      Timeline timeline =
          new Timeline(
              new KeyFrame(
                  Duration.seconds(2),
                  (ev) -> {
                    keypadLabel.setText("_ _ _ _");
                  }));
      timeline.play();
    }*/
  }

  @FXML
  private void clickDeleteButton(MouseEvent event) {
    /*if (GameState.isDoorCodeEntered) {
      return;
    }*/

    keypadLabel.setText("_ _ _ _");
  }

  /**
   * Counts the number of occurences of a character in a string.
   *
   * @param text the string to search
   * @param character the character to search for
   * @return the number of occurences of the character in the string
   */
  private int countOccurences(String text, char character) {
    int count = 0;
    for (int i = 0; i < text.length(); i++) {
      if (text.charAt(i) == character) {
        count++;
      }
    }
    return count;
  }

  /**
   * Handles mouse hovering over a button.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverButton(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectID = object.getId();

    actionLabel.setText("Press the " + objectID + " button");
  }

  /**
   * Handles mouse unhovering over a button.
   *
   * @param event the mouse event
   */
  @FXML
  private void unhoverObject(MouseEvent event) {
    actionLabel.setText("");
  }

  @FXML
  private void hoverArrow(MouseEvent event) {
    mainRoomArrow.setOpacity(1);
    actionLabel.setText("Go to front of room");
  }

  @FXML
  private void unhoverArrow(MouseEvent event) {
    mainRoomArrow.setOpacity(0.7);
    actionLabel.setText("");
  }

  @FXML
  private void switchToMainRoom() {
    GameState.switchRoom("mainroom");
  }
}
