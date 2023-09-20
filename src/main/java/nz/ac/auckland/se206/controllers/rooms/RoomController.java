package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
  @FXML protected Rectangle grayRectangle;
  @FXML protected Rectangle okayRectangle;
  @FXML protected ImageView arrow1;
  @FXML protected ImageView arrow2;
  @FXML protected ImageView background;
  @FXML protected ImageView partFoundTitleImage;
  @FXML protected ImageView partFoundContentImage;
  @FXML protected ImageView partFoundOkayImage;
  @FXML protected ImageView alienImage;
  protected Thread backgroundThread;
  protected Thread flagThread;
  protected Thread alienThread;
  protected boolean visited;

  @FXML
  private void initialize() {
    // Check if minigame popup shown after solved
    if (!GameState.getPartFoundPopupShown() && GameState.getMinigameSolved()) {
      GameState.setPartFoundPopupShown();
      showPopup();
    }
    animate();
    alienThread = new Thread(() -> animateAlien());
    alienThread.setDaemon(true);
    alienThread.start();
  }

  protected void stopThreads() {
    if (backgroundThread != null) {
      backgroundThread.interrupt();
    }
    if (flagThread != null) {
      flagThread.interrupt();
    }
    if (alienThread != null) {
      alienThread.interrupt();
    }
  }

  /** Show the part found popup */
  protected void showPopup() {
    // Set the correct image for the contents of the popup
    switch (GameState.partsFound) {
      case 1:
        partFoundContentImage.setImage(new Image("/images/rooms/partsleft2.png"));
        break;
      case 2:
        partFoundContentImage.setImage(new Image("/images/rooms/partsleft1.png"));
        break;
      case 3:
        partFoundContentImage.setImage(new Image("/images/rooms/partsleft0.png"));
        break;
      default:
        partFoundContentImage.setImage(new Image("/images/rooms/partsleft0.png"));
        break;
    }

    // Show the popup
    grayRectangle.setVisible(true);
    okayRectangle.setVisible(true);
    partFoundTitleImage.setVisible(true);
    partFoundContentImage.setVisible(true);
    partFoundOkayImage.setVisible(true);
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
   * Handles the click event on the okay button in the part found popup.
   *
   * @param event the mouse event
   */
  @FXML
  protected void clickOkay(MouseEvent event) {
    grayRectangle.setVisible(false);
    okayRectangle.setVisible(false);
    partFoundTitleImage.setVisible(false);
    partFoundContentImage.setVisible(false);
    partFoundOkayImage.setVisible(false);
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
      GameState.incrementPartsFound();
      GameState.isObjectFound = true;
      showPopup();
    } else {
      // Not the correct object. Provide some flavour text.
      GameState.setAlienHead();
      GameState.sayFlavourText(objectID);
    }
  }

  /**
   * Handles the hover event on an object.
   *
   * @param event the mouse event
   */
  @FXML
  protected void hoverObject(MouseEvent event) {
    Rectangle object = (Rectangle) event.getSource();
    String objectID = object.getId();

    Scene scene = object.getScene();
    ImageView image = (ImageView) scene.lookup("#" + objectID);
    image.setImage(new Image("/images/objects/" + objectID + "_selected.png"));

    actionLabel.setText("Search object");
  }

  /**
   * Handles the unhover event on an object.
   *
   * @param event the mouse event
   */
  @FXML
  protected void unhoverObject(MouseEvent event) {
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
  protected void clickMinigame(MouseEvent event) {

    // Check if minigame has been solved
    if (GameState.getMinigameSolved()) {
      showPopup();
      return;
    }
    stopThreads();
    GameState.inMinigame = true;
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
    stopThreads();
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
    actionLabel.setText("Go to previous room");
  }

  /**
   * Handles the hover event on the right arrow.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverArrow2(MouseEvent event) {
    arrow2.setOpacity(1);
    actionLabel.setText("Go to next room");
  }

  @FXML
  protected void hoverAlien(MouseEvent event) {
    actionLabel.setText("Talk to alien");
  }

  @FXML
  protected void unhoverAlien(MouseEvent event) {
    actionLabel.setText("");
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

  protected void animateAlien() {
    while (true) {
      while (GameState.getAlienTalking()) {
        try {

          changeAlienImage(alienImage.getId(), "_talking1");
          Thread.sleep(200);
          changeAlienImage(alienImage.getId(), "_talking2");
          Thread.sleep(200);
          changeAlienImage(alienImage.getId(), "_talking3");
          Thread.sleep(200);
          changeAlienImage(alienImage.getId(), "_talking2");
          Thread.sleep(200);
          if (!GameState.getAlienTalking()) {
            changeAlienImage(alienImage.getId(), "");
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Changes the image of the alien.
   *
   * @param objectID the ID of the alien
   * @param stageOfAnimation the stage of the animation
   */
  public void changeAlienImage(String objectID, String stageOfAnimation) {
    alienImage.setImage(new Image("/images/" + objectID + stageOfAnimation + ".png"));
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
