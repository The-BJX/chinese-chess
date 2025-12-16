package sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameSoundFX {

    // Define constants for the paths
    private static final String SELECT_SOUND = "/audio/tap_1.mp3";
    private static final String DESELECT_SOUND = "/audio/tap2.mp3";
    private static final String MOVE_SOUND = "/audio/plant.mp3";
    private static final String CHECK_SOUND = "/audio/siren.mp3";
    private static final String VICTORY_SOUND = "/audio/finalwave.mp3";

    /**
     * Helper method to load and play a sound once.
     * @param resourcePath The path to the sound file in resources.
     */
    private void playSound(String resourcePath) {
        try {
            // 1. Load the media
            String mediaUrl = getClass().getResource(resourcePath).toExternalForm();
            Media sound = new Media(mediaUrl);

            // 2. Create the MediaPlayer
            MediaPlayer mediaPlayer = new MediaPlayer(sound);

            // 3. Configure to dispose (clean up) after playing is finished
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.dispose();
            });

            // 4. Play the sound
            mediaPlayer.setVolume(1.0); // Set volume for effects
            mediaPlayer.play();

        } catch (Exception e) {
            System.err.println("Error playing sound effect " + resourcePath + ": " + e.getMessage());
            // This is useful for debugging missing files
        }
    }

    // --- Public methods to trigger sounds ---

    public void playSelectSound() {
        playSound(SELECT_SOUND);
    }

    public void playDeselectSound() {
        playSound(DESELECT_SOUND);
    }

    public void playMoveSound() {
        playSound(MOVE_SOUND);
    }

    public void playCheckSound() {
        playSound(CHECK_SOUND);
    }

    public void playVictorySound(){
        playSound(VICTORY_SOUND);
    }
}