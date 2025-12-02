package chinese_chess;

import Core.Board;
import Game.Game;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class GraphicElements {
    Pane WindowRoot,GameRoot,ChessBoard,PlayerBlack,PlayerRed,GamePane;
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

    Button NewGame, LoadFromSave;

    Label WhosTurn;

    Game game;
}
