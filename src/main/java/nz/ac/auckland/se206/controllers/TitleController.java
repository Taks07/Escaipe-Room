package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class TitleController {

  /**
   * Switches to the game view.
   *
   * @throws IOException if the game view cannot be loaded
   */
  @FXML
  protected void switchToGame() {
    try {
      // Start the game by switching to the game view, starting a timer, setting the riddle answer,
      // and providing context
      startGame();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void startGame() throws IOException {
    App.setRoot("game");
    GameTimer.startTimer(120);
    GameState.resetGameState();
    GameState.setRandomCurrRiddleAnswer();
    GameState.createDoorCode();
    GameState.setRandomRooms();
    GameState.askGPT(GptPromptEngineering.getGameContext());
  }

  /** Exits the application. */
  @FXML
  protected void exitApplication() {
    System.exit(0);
  }
}
