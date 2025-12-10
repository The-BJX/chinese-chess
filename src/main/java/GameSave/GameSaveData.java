package GameSave;


import java.io.Serializable;
import java.util.List;

public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String username;
    public final List<MoveRecord> moveHistory;

    public GameSaveData(String username, List<MoveRecord> moveHistory) {
        this.username = username;
        this.moveHistory = moveHistory;
    }
}
