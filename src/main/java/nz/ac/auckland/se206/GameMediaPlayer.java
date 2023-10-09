package nz.ac.auckland.se206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** Utility class for playing game sounds. */
public class GameMediaPlayer {
  private static MediaPlayer mediaPlayer;

  /** Plays a notification sound when the GPT API response is recieved. */
  public static void playNotificationSound() {
    if (GameState.isMuted) {
      return;
    }

    Media media = new Media(App.class.getResource("/sounds/notification.mp3").toString());
    mediaPlayer = new MediaPlayer(media);
    mediaPlayer.play();
  }
}
