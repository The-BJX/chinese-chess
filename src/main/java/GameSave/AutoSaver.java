package GameSave;
import java.time.LocalDateTime;
import java.io.*;
import java.util.List;


public class AutoSaver {
    ChineseChessDataSaver dataSaver=new ChineseChessDataSaver();

    LocalDateTime currentDateTime = LocalDateTime.now();
    String LocalDateTimeString = currentDateTime.toString();
    String fileName = "autosave_" + LocalDateTimeString.replace(":", "-") + ".dat";
    public void autoSaveGameData(List<MoveRecord> moveHistory) throws Exception {
        dataSaver.saveGameData(moveHistory, fileName);
    }
}
