package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

// to call this translatorroomcontroller in another class use this code:
// FXMLLoader translatorRoomLoader = loadFxml("translatorroom");
// mainBorderPane.setCenter(translatorRoomLoader.load());
// timerLabel.textProperty().bind(GameTimer.timerLabel);
// TranslatorRoomController translatorRoomController = translatorRoomLoader.getController();
// translatorRoomController.transformStringAndDisplay("String you want to convert");

public class TranslatorRoomController extends RoomController {

  String input = "";
  RandomSignConverter signConverter;
  @FXML private TextArea textArea;
  @FXML private Button translateButton;

  /**
   * Handles the event when the translate button is clicked, it translates the alien message into
   * english.
   *
   * @param event The action event triggered by the translate button.
   * @throws ApiProxyException If there is an error communicating with the API proxy.
   * @throws IOException If there is an I/O error.
   */
  @FXML
  private void onClickTranslate(ActionEvent event) throws ApiProxyException, IOException {
    signConverter.randomTransform(input); // Call the method on the signConverter
  }

  // TODO: Bug where you can only click translate button in random rooms but not in main room
  /**
   * This class is used to convert a string into a string of random characters. It also has a method
   * to transform the string into the original string.
   */
  public class RandomSignConverter {
    String converted;
    String randomSigns =
        "\u0E04\u0E52\u03C2\u0E54\u0454\u0166\uFEEE\u0452\u0E40\u05DF\u043A\u026D\u0E53\u0E20\u0E4F\u05E7\u1EE3\u0433\u0E23\u0547\u0E22\u028B\u0E2C\u05D0\u05E5\u0579\u0E04\u0E52\u03C2\u0E54\u0454\u0166\uFEEE\u0452\u0E40\u05DF\u043A\u026D\u0E53\u0E20\u0E4F\u05E7\u1EE3\u0433\u0E23\u0547\u0E22\u05E9\u0E2C\u05D0\u05E5\u0579";

    String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private TextArea textArea;

    public RandomSignConverter(String input, TextArea textArea) {
      this.textArea = textArea;
      converted = convertToRandomSigns(input);
      textArea.setText(converted);
    }

    /**
     * Converts a string into the correctponding alien string.
     *
     * @param input The string to be converted.
     * @return The converted string.
     */
    public String convertToRandomSigns(String input) {
      StringBuilder converted = new StringBuilder();
      translateButton.setDisable(false);

      for (char c : input.toCharArray()) {
        switch (c) {
          case ' ':
          case ',':
          case '.':
          case '!':
          case '?':
            converted.append(c);
            break;
          default:
            int index = alphabet.indexOf(c);
            if (index >= 0 && index < randomSigns.length()) {
              char randomChar = randomSigns.charAt(index);
              converted.append(randomChar);
            } else {
              converted.append(c);
            }
        }
      }

      return converted.toString();
    }

    /**
     * Transforms the alien string back into its original form and displays it in the TextField with
     * each letter taking 0.06 seconds to transform.
     *
     * @param word The original string.
     */
    public void randomTransform(String word) {
      StringBuilder currentMessage = new StringBuilder(converted);

      new Thread(
              () -> {
                translateButton.setDisable(true);
                for (int i = 0; i < word.length(); i++) {
                  char alienChar = word.charAt(i);
                  if (alienChar == ' ') {
                    continue; // Ignore spaces
                  }

                  char randomChar = word.charAt(i);

                  // Replace the corresponding character in the current message
                  currentMessage.setCharAt(i, randomChar);

                  // Update the TextField with the current message
                  Platform.runLater(() -> textArea.setText(currentMessage.toString()));

                  try {
                    Thread.sleep(60); // Sleep for 0.06 seconds
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }
              })
          .start();
    }
  }

  /**
   * Call this method from another class to display the string in the translating room.
   *
   * @param input The string to be transformed.
   */
  public void transformStringAndDisplay(String input) {
    this.input = input;
    signConverter = new RandomSignConverter(input, textArea);
  }
}
