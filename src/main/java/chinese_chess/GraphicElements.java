package chinese_chess;

import AIMove.AIMove;
import Core.Board;
import Game.Game;
import GameDialogues.GameDialogue;
import UserData.UserDataKeeper;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GraphicElements {
    public Pane WindowRoot;
    Stage stage;
    Pane GameRoot;
    Pane ChessBoard;
    Pane PlayerBlack;
    Pane PlayerRed;
    Pane GamePane;
    VBox BlackMenu,RedMenu,GameMenu;
    Label bLabel, rLabel,gLabel;
    Line[] UpVerticleLine;
    Line[] DownVerticleLine;
    Line[] HorizontalLine;
    Line[] CrossLine;

    Pane PaneChuhe, PaneHanjie;
    Label Chuhe, Hanjie;//楚河汉界
    String StyleOfMiddleBoard;

    Pane BoardSurface;//真正行棋的地方，刷新棋盘的时候clear(), 然后把棋子一个个加回来

    Button NewGame, LoadFromSave, SaveGame;
    Button RedRegret, BlackRegret;
    public Button SignIn;

    Label WhosTurn;

    public Game game;

    public GameDialogue Dialogue;
    public String usernameCache;//用于传递输入框文字
    public String passwordCache;//用于传递输入框文字

    public UserDataKeeper userDataKeeper;

    public String Username;
    public Label UserLabel;
    public Button Register;
    public Button BlackAskTie;
    public Button RedAskTie;

    public boolean isViewingRecord;//是否是在看demo
    public Button ViewRecord,LastStep,NextStep,GotoStart;//复盘
    public VBox RecordControlMenu;
    public HBox RecordControlButtons;
    public Label CurrentStep;

    public Button BlackSurrender, RedSurrender;//投降和求和
    public Font ChessFont;

    public AIMove aiMove;
    public Button BlackAIAssist, RedAIAssist;

    public Button Altermode;//摆棋
}
