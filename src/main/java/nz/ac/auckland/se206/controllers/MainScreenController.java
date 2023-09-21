package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;

public class MainScreenController extends TitleController {

  @FXML private ImageView flyingrocket;
  @FXML private ImageView mainbackground;
  @FXML private ImageView startImage;

  /**
   * Handles the click event on the start button
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the title view
   */
  @FXML
  private void clickStart(MouseEvent event) throws IOException {

    backgroundThread.interrupt();
    rocketThread.interrupt();

    try {
      App.setRoot("title");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
