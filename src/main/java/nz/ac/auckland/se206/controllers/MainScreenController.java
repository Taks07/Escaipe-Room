package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;

/**
 * Controller for the title screen. This extends RoomController so that it can use the same fade
 * animation. The main screen is the first screen the user sees when they open the game.
 */
public class MainScreenController extends TitleController {

  @FXML private ImageView flyingrocket;
  @FXML private ImageView mainbackground;
  @FXML private ImageView startImage;

  /**
   * Handles the click event on the start button.
   *
   * @param event The mouse event triggering the click.
   * @throws IOException If there is an error loading the title view.
   */
  @FXML
  private void clickStart(MouseEvent event) throws IOException {
    // Interrupt any background and rocket threads (if running).
    backgroundThread.interrupt();
    rocketThread.interrupt();

    try {
      // Navigate to the title view.
      App.setRoot("title");
    } catch (IOException e) {
      e.printStackTrace(); // Handle the exception, e.g., log or display an error message.
    }
  }
}
