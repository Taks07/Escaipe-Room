package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.util.Duration;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Utility class for controlling the game timer. */
public class GameTimer {
  /** Indicates what to display on the timer label. */
  public static StringProperty timerLabel = new SimpleStringProperty();

  /** Controls the timer. */
  public static Timeline timerTimeline = new Timeline();

  /** Indicates the number of seconds left on the timer. */
  private static int secondsLeft;

  /** Text to speech object. */
  private static TextToSpeech tts = new TextToSpeech();

  /** Thread to run the text to speech object. */
  private static Thread ttsThread;

  static {
    // Set up timeline to call countDown() every second
    timerTimeline
        .getKeyFrames()
        .add(
            new KeyFrame(
                Duration.seconds(1),
                (event) -> {
                  countDown();
                  if (secondsLeft % 30 == 0 || secondsLeft <= 10) {
                    sayTimeLeft();
                  }
                }));

    // Set up timeline to switch to title screen when time runs out
    timerTimeline.setOnFinished(
        (event) -> {
          try {
            GameState.gameWon = false;
            App.endGame();
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  /** Starts the timer in a new thread. This method should be called when the game starts. */
  public static void startTimer() {
    secondsLeft = GameState.timeLimit;
    timerLabel.setValue(getTimerLabel());
    timerTimeline.setCycleCount(GameState.timeLimit);
    timerTimeline.play();
    sayTimeLeft();
  }

  /** Decrements the timer by one second. */
  private static void countDown() {
    secondsLeft--;
    timerLabel.setValue(getTimerLabel());
  }

  /**
   * Gets the time left on the timer in a easier to read format.
   *
   * @return the string to display on the timer label, calculated from secondsLeft
   */
  private static String getTimerLabel() {
    int minutes = secondsLeft / 60;
    int seconds = secondsLeft % 60;
    return String.format("%2d:%02d", minutes, seconds);
  }

  /**
   * Announces the remaining time at 30-second intervals using text-to-speech (TTS) with an AI
   * voice.
   */
  private static void sayTimeLeft() {
    Task<Void> ttsTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Calculate remaining minutes and seconds
            int minutes = secondsLeft / 60;
            int seconds = secondsLeft % 60;

            // If there are less than 10 seconds remaining and no minutes left, announce seconds
            // only
            if (seconds <= 10 && minutes == 0) {
              tts.speak(String.valueOf(seconds));
              return null;
            }

            // Announce remaining time in a human-friendly format
            if (minutes == 0) {
              tts.speak(String.format("You have %d seconds left", seconds));
            } else if (seconds == 0) {
              tts.speak(String.format("You have %d minutes left", minutes));
            } else {
              tts.speak(String.format("You have %d minutes and %d seconds left", minutes, seconds));
            }
            return null;
          }
        };

    // Create a new thread for TTS task and start it
    ttsThread = new Thread(ttsTask);
    ttsThread.start();
  }

  /** Stops the timer and the TTS thread. */
  public static void stopTts() {
    tts.terminate();
    ttsThread.interrupt();
  }

  /** Stops the timer timeline and TTS thread. */
  public static void stopTimeline() {
    timerTimeline.stop();
  }
}
