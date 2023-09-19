package nz.ac.auckland.se206.controllers.minigames;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class RandRoom3MinigameController extends MinigameController {

  @FXML private ImageView[] bottomTeeth;

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

  private enum ToothSize {
    SMALL,
    MEDIUM,
    LARGE
  }

  private Image smallToothImage;
  private Image mediumToothImage;
  private Image largeToothImage;
  private Random random = new Random();

  public void initialize() {
    bottomTeeth =
        new ImageView[] {
          teeth11, teeth12, teeth13, teeth14, teeth15, teeth16, teeth17, teeth18, teeth19, teeth20
        };
    smallToothImage = new Image("/images/minigames/small.png");
    mediumToothImage = new Image("/images/minigames/medium.png");
    largeToothImage = new Image("/images/minigames/large.png");

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
  }

  @FXML
  public void clickTeeth(MouseEvent event) {
    ImageView clickedTooth = (ImageView) event.getSource();

    ToothSize currentState = ToothSize.SMALL;
    if (clickedTooth.getImage() == mediumToothImage) {
      currentState = ToothSize.MEDIUM;
    } else if (clickedTooth.getImage() == largeToothImage) {
      currentState = ToothSize.LARGE;
    }

    ToothSize nextState;
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
  }
}
