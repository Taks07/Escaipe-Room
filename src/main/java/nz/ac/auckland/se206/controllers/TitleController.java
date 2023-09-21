package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.GameTimer;
import nz.ac.auckland.se206.controllers.rooms.RoomController;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class TitleController extends RoomController {
  @FXML private ToggleGroup difficulty;
  @FXML private ToggleGroup timeLimit;
  @FXML private ImageView begin;
  @FXML private ImageView xImageView;
  @FXML private ImageView paragraph;
  @FXML private Rectangle block;

  @FXML private ImageView flyingrocket;
  @FXML private ImageView mainbackground;
  protected Thread rocketThread;

  @FXML
  private void initialize() {
    animate();
    if (flyingrocket != null) {
      animateRocket();
    }
  }

  /**
   * Switches to the game view.
   *
   * @throws IOException if the game view cannot be loaded
   */
  @FXML
  protected void switchToGame(MouseEvent event) {
    try {
      // Start the game by switching to the game view, starting a timer, setting the riddle answer,
      // and providing context
      startGame(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void clickX(MouseEvent event) {
    block.setVisible(false);
    paragraph.setVisible(false);
    xImageView.setVisible(false);

  }

  public void startGame(MouseEvent event) throws IOException {
    GameState.resetGameState();
    setDifficultyAndTimeLimit();
    GameTimer.startTimer();
    GameState.setRandomRooms();
    GameState.setRandomCurrRiddleAnswer();

    // makes a new scene and switches to it, but keeps the same stage
    ImageView image = (ImageView) event.getSource();
    Stage stage = (Stage) image.getScene().getWindow();
    App.startGame(stage);
    GameState.askGpt(GptPromptEngineering.getGameContext());
  }

  public void setDifficultyAndTimeLimit() {
    RadioButton difficultyOption = (RadioButton) difficulty.getSelectedToggle();
    RadioButton timeLimitOption = (RadioButton) timeLimit.getSelectedToggle();

    GameState.setHintsAllowed(difficultyOption.getId());
    GameState.setTimeLimit(Integer.parseInt(timeLimitOption.getText().substring(0, 1)));
  }

  @FXML
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
                    mainbackground.setImage(new Image("/images/title_screen/mainbackground2.png"));
                  });
              Thread.sleep(500);
              Platform.runLater(
                  () -> {
                    // Animate 2
                    mainbackground.setImage(new Image("/images/title_screen/mainbackground1.png"));
                  });
            }
          }
        };
    backgroundThread = new Thread(animation);

    backgroundThread.setDaemon(true);
    backgroundThread.start();
  }

  protected void animateRocket() {
    Task<Void> animation =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (true) {
              Thread.sleep(300);
              Platform.runLater(
                  () -> {
                    // Animate 1
                    flyingrocket.setImage(new Image("/images/title_screen/flyingrocket1.png"));
                  });
              Thread.sleep(300);
              Platform.runLater(
                  () -> {
                    // Animate 2
                    flyingrocket.setImage(new Image("/images/title_screen/flyingrocket2.png"));
                  });
              Thread.sleep(300);
              Platform.runLater(
                  () -> {
                    // Animate 2
                    flyingrocket.setImage(new Image("/images/title_screen/flyingrocket3.png"));
                  });
              Thread.sleep(300);
              Platform.runLater(
                  () -> {
                    // Animate 2
                    flyingrocket.setImage(new Image("/images/title_screen/flyingrocket2.png"));
                  });
            }
          }
        };
    rocketThread = new Thread(animation);

    rocketThread.setDaemon(true);
    rocketThread.start();
  }

  @FXML
  protected void hoverObject(MouseEvent event) {
    ImageView object = (ImageView) event.getSource();
    String objectId = object.getId();

    object.setImage(new Image("/images/objects/" + objectId + "_selected.png"));
  }

  @FXML
  protected void unhoverObject(MouseEvent event) {
    ImageView object = (ImageView) event.getSource();
    String objectId = object.getId();

    object.setImage(new Image("/images/objects/" + objectId + ".png"));
  }

  /** Exits the application. */
  @FXML
  protected void onExitApplication() {
    System.exit(0);
  }
}
