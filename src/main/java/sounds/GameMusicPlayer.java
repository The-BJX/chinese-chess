package sounds;
import javafx.scene.media.Media;
import java.io.File;

public class GameMusicPlayer {
    String musicKitName;

    public GameMusicPlayer(String musicKitName) {
        this.musicKitName = musicKitName;
    }

    void playWinningSound(){

    }
    void playLosingSound(){

    }
    void playCheckSound(){
        //将军，但不是将死
    }
    void playMoveSound(){

    }
    void playKillSound(){

    }
    private void playSoundInKit(String name){
        String path = new File(name+".mp3").toURI().toString();
        Media media = new Media(path);
    }
    void playRegret(){

    }
}
