package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.controllers.rooms.TranslatorRoomController;

/** Controller class for the main game view. */
public class GameController {
  /**
   * Loads the FXML file with the given name and return its loader.
   *
   * @param fxml the name of the FXML file to load
   * @return the FXMLLoader used to load the FXML file
   * @throws IOException if there is an error loading the FXML file
   */
  private static FXMLLoader loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  @FXML private BorderPane mainBorderPane;
  @FXML private Label timerLabel;

  /** Code that is run when first starting game */
  public void initialize() {
    try {
      FXMLLoader translatorRoomLoader = loadFxml("translatorroom");

      mainBorderPane.setBottom(translatorRoomLoader.load());

      timerLabel.textProperty().bind(GameTimer.timerLabel);

      TranslatorRoomController translatorRoomController = translatorRoomLoader.getController();

      // Change this to the string you want to display
      translatorRoomController.transformStringAndDisplay("Example Text For Alien Translation");

      mainBorderPane.setCenter(loadFxml("mainroom").load());

      FXMLLoader chatFxmlLoader = loadFxml("chat");

      mainBorderPane.setRight(chatFxmlLoader.load());

      // Get the controller of the chat pane and pass it to GameState
      GameState.setChatController(chatFxmlLoader.getController());

      timerLabel.textProperty().bind(GameTimer.timerLabel);

      GameState.setGameController(this);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void switchRoom(String fxml) {
    try {
      mainBorderPane.setCenter(loadFxml(fxml).load());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
