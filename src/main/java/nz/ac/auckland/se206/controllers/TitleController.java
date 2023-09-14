package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class TitleController extends MainScreenController {
  @FXML private ToggleGroup difficulty;
  @FXML private ToggleGroup timeLimit;
  @FXML private ImageView begin;
  @FXML private ImageView exit;
  @FXML private ImageView paragraph;
  @FXML private Rectangle block;

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

  @FXML
  public void clickX(MouseEvent event) {
    block.setVisible(false);
    paragraph.setVisible(false);
    exit.setVisible(false);
  }

  public void startGame() throws IOException {
    setDifficultyAndTimeLimit();
    App.setRoot("game");
    GameTimer.startTimer();
    GameState.resetGameState();
    GameState.setRandomRooms();
    GameState.setRandomCurrRiddleAnswer();
    GameState.askGPT(GptPromptEngineering.getGameContext());
  }

  public void setDifficultyAndTimeLimit() {
    RadioButton difficultyOption = (RadioButton) difficulty.getSelectedToggle();
    RadioButton timeLimitOption = (RadioButton) timeLimit.getSelectedToggle();

    GameState.setHintsAllowed(difficultyOption.getId());
    GameState.setTimeLimit(Integer.parseInt(timeLimitOption.getText().substring(0, 1)));
  }

  /** Exits the application. */
  @FXML
  protected void exitApplication() {
    System.exit(0);
  }
}
