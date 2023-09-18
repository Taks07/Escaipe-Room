package nz.ac.auckland.se206.controllers.minigames;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Plays the sequence game similar to simons game, where the player clicks on the buttons, and each
 * consequetive round it adds an extra button to the sequence once 5 rounds are played you win the
 * game
 */
public class RandRoom1MinigameController extends MinigameController {

  @FXML private GridPane gridPane;
  @FXML private Button button1;
  @FXML private Button button2;
  @FXML private Button button3;
  @FXML private Button button4;
  @FXML private Button button5;
  @FXML private Button button6;
  @FXML private Button button7;
  @FXML private Button button8;
  @FXML private Button button9;

  @FXML private ImageView image1;
  @FXML private ImageView image2;
  @FXML private ImageView image3;
  @FXML private ImageView image4;
  @FXML private ImageView image5;
  @FXML private ImageView image6;
  @FXML private ImageView image7;
  @FXML private ImageView image8;
  @FXML private ImageView image9;

  private Button[][] buttons;
  private ImageView[][] imageViews;
  private List<Integer> sequence;
  private int sequenceNum = 0;
  private int round = 0;

  public void initialize() {
    buttons =
        new Button[][] {
          {button1, button2, button3},
          {button4, button5, button6},
          {button7, button8, button9}
        };
    imageViews =
        new ImageView[][] {
          {image1, image2, image3},
          {image4, image5, image6},
          {image7, image8, image9}
        };

    sequence = new ArrayList<>();
    startNewRound();
  }

  /**
   * Starts a new round of the game, where it adds a new button to the sequence, and highlights the
   * sequence the button can not be the same button as the one before it
   */
  private void startNewRound() {
    round++;
    Random random = new Random();
    int lastNumber;
    if (sequence.isEmpty()) {
      lastNumber = -1;
    } else {
      lastNumber = sequence.get(sequence.size() - 1);
    }
    int newNumber;

    do {
      newNumber = random.nextInt(9);
    } while (newNumber == lastNumber);

    sequence.add(newNumber);
    sequenceNum = 0;

    highlightSequence();
  }

  /** Highlights the sequence of buttons that the player must click on */
  private void highlightSequence() {
    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    pause.setOnFinished(event -> highlightNextButton());
    pause.play();
  }

  /** Highlights the next button in the sequence */
  private void highlightNextButton() {
    if (sequenceNum < round) {
      // int buttonIndex = sequence.get(sequenceNum);
      // Button button = buttons[buttonIndex / 3][buttonIndex % 3];
      int imageIndex = sequence.get(sequenceNum);
      ImageView image = imageViews[imageIndex / 3][imageIndex % 3];

      image.setImage(new Image("images/minigames/yellow_button.png"));

      PauseTransition pauseBetween = new PauseTransition(Duration.seconds(0.5));
      pauseBetween.setOnFinished(
          event -> {
            image.setImage(new Image("images/minigames/unclicked_button.png"));
            sequenceNum++;
            highlightNextButton();
          });
      pauseBetween.play();
    } else {

      sequenceNum = 0;
    }
  }

  /**
   * Handles the button click event, where the player clicks on the buttons in the sequence if the
   * player clicks on the wrong button, the game ends
   *
   * @param event the mouse event
   * @throws IOException
   */
  @FXML
  private void handleButtonClick(MouseEvent event) {
    if (sequenceNum < sequence.size()) {
      Button clickedButton = (Button) event.getSource();
      int clickedIndex = getIndexFromButton(clickedButton);
      int expectedIndex = sequence.get(sequenceNum);

      if (clickedIndex == expectedIndex) {
        sequenceNum++;
        if (sequenceNum == sequence.size()) {
          if (sequence.size() == round) {
            if (round == 5) {
              setAllButtonsGreen();
              System.out.println("You win!");
              // TODO: if you win, change back to the randroom1controller
            } else {
              startNewRound();
            }
          }
        }
      } else {
        setAllButtonsRed();
        sequence.clear();
        sequenceNum = 0;
        round = 0;
        startNewRound();
      }
    }
  }

  /**
   * Gets the index of the button in the grid pane
   *
   * @param button the button to get the index of
   * @return the index of the button
   */
  private int getIndexFromButton(Button button) {
    for (int i = 0; i < buttons.length; i++) {
      for (int j = 0; j < buttons[i].length; j++) {
        if (buttons[i][j] == button) {
          return i * 3 + j;
        }
      }
    }
    return -1;
  }

  /** Sets all the buttons to red, indicating that the player has lost the game */
  private void setAllButtonsRed() {
    for (int i = 0; i < buttons.length; i++) {
      for (int j = 0; j < buttons[i].length; j++) {
        imageViews[i][j].setImage(new Image("images/minigames/red_button.png"));
      }
    }

    PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
    pause.setOnFinished(event -> setAllButtonsToDefaultColor());
    pause.play();
  }

  /** Sets all the buttons to green, indicating that the player has won the game */
  private void setAllButtonsGreen() {
    for (int i = 0; i < buttons.length; i++) {
      for (int j = 0; j < buttons[i].length; j++) {
        imageViews[i][j].setImage(new Image("images/minigames/green_button.png"));
      }
    }

    PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
    pause.setOnFinished(event -> setAllButtonsToDefaultColor());
    pause.play();
  }

  /** Sets all the buttons to the default colour */
  private void setAllButtonsToDefaultColor() {
    for (int i = 0; i < buttons.length; i++) {
      for (int j = 0; j < buttons[i].length; j++) {
        imageViews[i][j].setImage(new Image("images/minigames/unclicked_button.png"));
      }
    }
  }
}
