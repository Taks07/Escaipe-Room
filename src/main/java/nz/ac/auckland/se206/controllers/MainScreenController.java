package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.controllers.rooms.RoomController;

public class MainScreenController extends RoomController {

  @FXML private ImageView flyingrocket;
  @FXML private ImageView mainbackground;
  @FXML private ImageView startImage;
  private Thread rocketThread;

  @FXML
  private void initialize() {
    animate();
    if (flyingrocket != null) {
      animateRocket();
    }
  }

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
  private void hoverObject(MouseEvent event) {
    ImageView object = (ImageView) event.getSource();
    String objectID = object.getId();

    object.setImage(new Image("/images/objects/" + objectID + "_selected.png"));
  }

  @FXML
  private void unhoverObject(MouseEvent event) {
    ImageView object = (ImageView) event.getSource();
    String objectID = object.getId();

    object.setImage(new Image("/images/objects/" + objectID + ".png"));
  }
}
