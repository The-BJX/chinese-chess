package chinese_chess;

import Core.Board;
import Game.Game;
import GameDialogues.GameDialogue;
import UserData.UserDataKeeper;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class GraphicElements {
    public Pane WindowRoot;
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
    public Button RedSignIn, BlackSignIn;

    Label WhosTurn;

    Game game;

    GameDialogue Dialogue;
    public String usernameCache;//用于传递输入框文字
    public String passwordCache;//用于传递输入框文字

    public UserDataKeeper userDataKeeper;

    public String BlackUsername, RedUsername;
    public Label BlackUserLabel, RedUserLabel;
    public Button Register;
}
