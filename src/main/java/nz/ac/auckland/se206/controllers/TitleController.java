package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

/**
 * Controller for the title screen. This extends RoomController so that it can use the same fade
 * animation.
 */
public class TitleController extends RoomController {
  @FXML private ToggleGroup difficulty;
  @FXML private ToggleGroup timeLimit;
  @FXML private ImageView begin;
  @FXML private ImageView exit;
  @FXML private ImageView paragraph;
  @FXML private Rectangle block;
  @FXML private Button muteButton;
  @FXML private Button flavourTextButton;
  @FXML private ImageView flyingrocket;
  @FXML private ImageView mainbackground;
  @FXML private ImageView mute;
  @FXML private ImageView flavourTextImage;
  protected Thread rocketThread;

  /**
   * Initializes the controller, sets initial values, and adds event listeners for the mute and
   * flavour text buttons.
   */
  @FXML
  private void initialize() {
    animate();
    if (flyingrocket != null) {
      animateRocket();
    }

    if (muteButton != null) {
      setMuteButtonText();
    }

    if (flavourTextButton != null) {
      setFlavourTextButtonText();
    }
  }

  /**
   * Mutes/unmutes the game audio.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickMute(ActionEvent event) {
    GameState.toggleMute();
    setMuteButtonText();
  }

  /**
   * Enables or disables object flavour text.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onClickFlavourText(ActionEvent event) {
    GameState.toggleFlavourText();
    setFlavourTextButtonText();
  }

  /**
   * Switches to the game view.
   *
   * @throws IOException if the game view cannot be loaded
   */
  @FXML
  protected void switchToGame(MouseEvent event) {
    // Start the game by switching to the game view, starting a timer, setting the riddle answer,
    // and providing context
    fadeOut();
    ft.setOnFinished(
        event2 -> {
          fadeRectangle.setVisible(false);
          try {
            startGame(event);
          } catch (IOException e) {
            System.out.println("I/O Exception");
          }
        });

    if (rocketThread != null) {
      rocketThread.interrupt();
    }
    backgroundThread.interrupt();

    ft.play();
  }

  /**
   * Closes the background paragraph.
   *
   * @param event the event triggered by clicking the X
   */
  @FXML
  public void clickX(MouseEvent event) {
    block.setVisible(false);
    paragraph.setVisible(false);
    exit.setVisible(false);
  }

  /**
   * Starts the game by switching to the game view, starting a timer, setting the riddle answer, and
   * providing context to the AI.
   *
   * @param event the event that triggered this method
   * @throws IOException if the game view cannot be loaded
   */
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

  /** Animates the background of the title screen. */
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

  /** Animates the rocket on the main screen. */
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

  /**
   * Changes the object to selected image when hovered over.
   *
   * @param event the event that triggered this method
   */
  @FXML
  protected void hoverObject(MouseEvent event) {
    ImageView object = (ImageView) event.getSource();
    String objectId = object.getId();

    object.setImage(new Image("/images/objects/" + objectId + "_selected.png"));
  }

  /**
   * Changes the object to unselected image when not hovered over.
   *
   * @param event the event that triggered this method
   */
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

  /** Sets the text of the mute button according to GameState.isMuted. */
  private void setMuteButtonText() {
    String name = (GameState.isMuted) ? "unmute" : "mute";
    setButtonImage(name, mute);
  }

  /** Sets the text of the flavour text button according to GameState.isFlavourTextEnabled. */
  private void setFlavourTextButtonText() {
    String name = (GameState.isFlavourTextEnabled) ? "disable_flavour_text" : "enable_flavour_text";
    setButtonImage(name, flavourTextImage);
  }

  @FXML
  protected void hoverFlavourTextButton() {
    String name =
        (GameState.isFlavourTextEnabled)
            ? "disable_flavour_text_selected"
            : "enable_flavour_text_selected";
    setButtonImage(name, flavourTextImage);
  }

  @FXML
  protected void unhoverFlavourTextButton() {
    String name = (GameState.isFlavourTextEnabled) ? "disable_flavour_text" : "enable_flavour_text";
    setButtonImage(name, flavourTextImage);
  }

  /** Changes the mute button to selected image when hovered over. */
  @FXML
  protected void hoverMuteButton() {
    String name = (GameState.isMuted) ? "unmute_selected" : "mute_selected";
    setButtonImage(name, mute);
  }

  /** Changes the mute button to unselected image when not hovered over. */
  @FXML
  protected void unhoverMuteButton() {
    String name = (GameState.isMuted) ? "unmute" : "mute";
    setButtonImage(name, mute);
  }

  protected void setButtonImage(String name, ImageView image) {
    image.setImage(new Image("/images/objects/" + name + ".png"));
  }
}
