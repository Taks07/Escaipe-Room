package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;
  private static Stage stage;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "mainscreen" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/mainscreen.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    stage.setOnCloseRequest(
        (event) -> {
          System.exit(0);
        });
    Parent root = loadFxml("mainscreen");
    scene = new Scene(root, 840, 510);
    stage.setScene(scene);
    stage.setY(0);
    stage.show();
    root.requestFocus();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "game" scene.
   *
   * @param stageInput The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/game.fxml" is not found.
   */
  public static void startGame(final Stage stageInput) throws IOException {

    // Set the stage to the input stage.
    stage = stageInput;
    stage.setOnCloseRequest(
        (event) -> {
          // Exit the application when the window is closed.
          System.exit(0);
        });

    // Load the game view at width 840 and height 745.
    Parent root = loadFxml("game");
    scene = new Scene(root, 840, 745);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  /**
   * This method ends the game and loads the results screen.
   *
   * @throws IOException If "src/main/resources/fxml/results.fxml" is not found.
   */
  public static void endGame() throws IOException {

    // Interrupt any background and rocket threads (if running).
    stage.setOnCloseRequest(
        (event) -> {
          System.exit(0);
        });

    // Navigate to the results view. (This is the only place where the results view is loaded.)
    Parent root = loadFxml("results");
    scene = new Scene(root, 840, 510);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }
}
