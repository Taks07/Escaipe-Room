package nz.ac.auckland.se206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameMediaPlayer {
  private static MediaPlayer mediaPlayer;

  public static void playNotificationSound() {
    if (GameState.isMuted) {
      return;
    }

    Media media = new Media(App.class.getResource("/sounds/notification.mp3").toString());
    mediaPlayer = new MediaPlayer(media);
    mediaPlayer.play();
  }
}
