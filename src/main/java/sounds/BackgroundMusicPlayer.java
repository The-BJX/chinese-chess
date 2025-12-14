package sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BackgroundMusicPlayer {

    private MediaPlayer mediaPlayer;
    private final String MUSIC_FILE_PATH = "/audio/bg_music.mp3";

    public void startMusic() {
        try {
            // 1. Get the resource URL. Uses ClassLoader to find the file in resources.
            // The .toExternalForm() is required for Media to correctly load resources from inside a JAR.
            String mediaUrl = getClass().getResource(MUSIC_FILE_PATH).toExternalForm();
            Media sound = new Media(mediaUrl);

            // 2. Create the MediaPlayer
            mediaPlayer = new MediaPlayer(sound);

            // 3. Configure for looping (essential for BGM)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            // Set volume (optional)
            mediaPlayer.setVolume(0.4);

            // 4. Start playback
            mediaPlayer.play();

        } catch (Exception e) {
            System.err.println("Error loading or playing music: " + e.getMessage());
            System.err.println("Check if 'bg_music.mp3' exists in 'src/main/resources/audio/' and is an MP3 file.");
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // Important for releasing system resources
        }
    }
}