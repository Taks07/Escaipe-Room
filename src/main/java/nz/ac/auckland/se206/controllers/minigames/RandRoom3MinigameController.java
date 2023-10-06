package nz.ac.auckland.se206.controllers.minigames;

import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/** Controller for the random tooth resizing minigame in Room 3. */
public class RandRoom3MinigameController extends MinigameController {

  // Enumeration to represent tooth size options
  private enum ToothSize {
    SMALL,
    MEDIUM,
    LARGE
  }

  // The tooth images of different sizes
  private Image smallToothImage;
  private Image mediumToothImage;
  private Image largeToothImage;
  private Random random = new Random();
  private boolean hasPlayerWon = false;

  // Arrays to store top and bottom teeth ImageView components
  @FXML private ImageView[] topTeeth;
  @FXML private ImageView[] bottomTeeth;

  // The ImageView components for the top and bottom teeth
  @FXML private ImageView teeth1;
  @FXML private ImageView teeth2;
  @FXML private ImageView teeth3;
  @FXML private ImageView teeth4;
  @FXML private ImageView teeth5;
  @FXML private ImageView teeth6;
  @FXML private ImageView teeth7;
  @FXML private ImageView teeth8;
  @FXML private ImageView teeth9;
  @FXML private ImageView teeth10;
  @FXML private ImageView teeth11;
  @FXML private ImageView teeth12;
  @FXML private ImageView teeth13;
  @FXML private ImageView teeth14;
  @FXML private ImageView teeth15;
  @FXML private ImageView teeth16;
  @FXML private ImageView teeth17;
  @FXML private ImageView teeth18;
  @FXML private ImageView teeth19;
  @FXML private ImageView teeth20;

  /**
   * Initializes the controller, sets initial values, and randomly assigns tooth images to top and
   * bottom teeth.
   */
  public void initialize() {
    fadeIn();
    isSolved = false;

    // Arrays to store top and bottom teeth ImageView components
    topTeeth =
        new ImageView[] {
          teeth1, teeth2, teeth3, teeth4, teeth5, teeth6, teeth7, teeth8, teeth9, teeth10
        };
    bottomTeeth =
        new ImageView[] {
          teeth11, teeth12, teeth13, teeth14, teeth15, teeth16, teeth17, teeth18, teeth19, teeth20
        };

    // Load tooth images of different sizes
    smallToothImage = new Image("/images/minigames/small.png");
    mediumToothImage = new Image("/images/minigames/medium.png");
    largeToothImage = new Image("/images/minigames/large.png");

    // Randomly assign tooth images to both top and bottom teeth
    for (int i = 0; i < 10; i++) {
      int newNum = random.nextInt(3);
      if (newNum == 0) {
        bottomTeeth[i].setImage(smallToothImage);
      } else if (newNum == 1) {
        bottomTeeth[i].setImage(mediumToothImage);
      } else {
        bottomTeeth[i].setImage(largeToothImage);
      }
    }

    for (int i = 0; i < 10; i++) {
      int newNum = random.nextInt(3);
      if (newNum == 0) {
        topTeeth[i].setImage(smallToothImage);
      } else if (newNum == 1) {
        topTeeth[i].setImage(mediumToothImage);
      } else {
        topTeeth[i].setImage(largeToothImage);
      }
    }
  }

  /**
   * Handles the mouse click event on teeth ImageView components to change their size.
   *
   * @param event The mouse click event.
   */
  @FXML
  public void clickTeeth(MouseEvent event) {
    if (hasPlayerWon) {
      return;
    }

    ImageView clickedTooth = (ImageView) event.getSource();

    // Determine the current size of the clicked tooth
    ToothSize currentState = ToothSize.SMALL;
    if (clickedTooth.getImage() == mediumToothImage) {
      currentState = ToothSize.MEDIUM;
    } else if (clickedTooth.getImage() == largeToothImage) {
      currentState = ToothSize.LARGE;
    }

    ToothSize nextState;
    // Determine the next size of the tooth based on the current size
    switch (currentState) {
      case SMALL:
        nextState = ToothSize.MEDIUM;
        break;
      case MEDIUM:
        nextState = ToothSize.LARGE;
        break;
      case LARGE:
        nextState = ToothSize.SMALL;
        break;
      default:
        nextState = ToothSize.SMALL;
        break;
    }

    // Set the image of the clicked tooth to the next size
    switch (nextState) {
      case SMALL:
        clickedTooth.setImage(smallToothImage);
        break;
      case MEDIUM:
        clickedTooth.setImage(mediumToothImage);
        break;
      case LARGE:
        clickedTooth.setImage(largeToothImage);
        break;
      default:
        break;
    }

    // Check if the player has won the game
    if (areAllTeethMatching()) {
      hasPlayerWon = true;

      // Pause for 1 second before ending the game
      PauseTransition pause = new PauseTransition(Duration.seconds(1));
      pause.setOnFinished(event2 -> endGame());
      pause.play();
    }
  }

  /**
   * Helper method to check if all top and bottom teeth match in size.
   *
   * @return True if all teeth match in size, false otherwise.
   */
  private boolean areAllTeethMatching() {
    for (int i = 0; i < bottomTeeth.length; i++) {
      if (bottomTeeth[i].getImage() != topTeeth[i].getImage()) {
        return false;
      }
    }
    return true;
  }
}
