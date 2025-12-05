package chinese_chess;

import data.Position;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GraphicController {
    static private double MENU_PADDING = ConstantValues.MENU_PADDING;
    private static GridPoint Selection = new GridPoint(-1,-1);
    static double GridWidth=1.0;
    public static boolean HoverChangedFlag = false;

    static GridPoint getSelection(){
        return Selection;
    }
    static void setSelection(GridPoint u){
        Selection = u;
    }
    static void initGraphics(Stage stage, GraphicElements elements){
        elements.WindowRoot = new Pane();
        elements.WindowRoot.setStyle("-fx-background-color: black");


        elements.GameRoot = new Pane();
        elements.GameRoot.setStyle("-fx-background-color: white");
        elements.WindowRoot.getChildren().add(elements.GameRoot);

        // 创建一个固定长宽比的容器
        elements.ChessBoard = new Pane();
        elements.ChessBoard.setStyle("-fx-background-color: burlywood; -fx-border-color: navy;");
        elements.ChessBoard.setOnMouseClicked(mouseEvent -> {
            setSelection(new GridPoint(-1,-1));
            System.out.println("Selection canceled when clicking board");
            if(elements.game.getBoard().getSelectedPosition()!=null){
                elements.game.getBoard().getPieceAt(elements.game.getBoard().getSelectedPosition()).isSelected=false;
                elements.game.getBoard().setSelectedPosition(null);
            }
            try {
                refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Label label = new Label("chessboard");
        label.setLayoutX(10);
        label.setLayoutY(10);
        elements.ChessBoard.getChildren().add(label);

        //框框
        elements.PlayerBlack = new Pane();
        elements.PlayerBlack.setStyle("-fx-border-color:black;-fx-border-width:5px");
        elements.PlayerRed = new Pane();
        elements.PlayerRed.setStyle("-fx-border-color:red;-fx-border-width:5px");
        elements.GamePane = new Pane();
        elements.GamePane.setStyle("-fx-border-color:blue;-fx-border-width:5px");

        //菜单
        elements.BlackMenu = new VBox();
        elements.BlackMenu.setPadding(new Insets(MENU_PADDING,MENU_PADDING,MENU_PADDING,MENU_PADDING));
        elements.RedMenu = new VBox();
        elements.RedMenu.setPadding(new Insets(MENU_PADDING,MENU_PADDING,MENU_PADDING,MENU_PADDING));
        elements.GameMenu = new VBox();
        elements.GameMenu.setPadding(new Insets(MENU_PADDING,MENU_PADDING,MENU_PADDING,MENU_PADDING));

        elements.PlayerBlack.getChildren().add(elements.BlackMenu);
        elements.PlayerRed.getChildren().add(elements.RedMenu);
        elements.GamePane.getChildren().add(elements.GameMenu);

        //两边文字标签
        elements.bLabel = new Label("Black Player");
        elements.bLabel.setWrapText(true);
        elements.bLabel.setStyle("-fx-font-size: 20; -fx-text-fill: black;");
        elements.BlackMenu.getChildren().add(elements.bLabel);

        elements.rLabel = new Label("Red Player");
        elements.rLabel.setWrapText(true);
        elements.rLabel.setStyle("-fx-font-size: 20; -fx-text-fill: red;");
        elements.RedMenu.getChildren().add(elements.rLabel);

        elements.gLabel = new Label("Game Menu");
        elements.gLabel.setWrapText(true);
        elements.gLabel.setStyle("-fx-font-size: 20; -fx-text-fill: blue;");
        elements.GameMenu.getChildren().add(elements.gLabel);

        elements.UpVerticleLine = new Line[9];
        elements.DownVerticleLine = new Line[9];
        elements.HorizontalLine = new Line[10];
        for (int i = 0; i < 9; i++) {
            elements.UpVerticleLine[i]=new Line();
            elements.DownVerticleLine[i]=new Line();
            elements.ChessBoard.getChildren().add(elements.UpVerticleLine[i]);
            elements.ChessBoard.getChildren().add(elements.DownVerticleLine[i]);
        }
        for (int i = 0; i < 10; i++) {
            elements.HorizontalLine[i]=new Line();
            elements.ChessBoard.getChildren().add(elements.HorizontalLine[i]);
        }
        elements.CrossLine=new Line[4];
        for (int i = 0; i < 4; i++) {
            elements.CrossLine[i]=new Line();
            elements.ChessBoard.getChildren().add(elements.CrossLine[i]);
        }

        elements.WhosTurn = new Label("");
        elements.WhosTurn.setStyle("-fx-font-size: 16; -fx-text-fill: black;");
        elements.GameMenu.getChildren().add(elements.WhosTurn);
        elements.WhosTurn.setText("请红方先手");
        elements.gLabel.setWrapText(true);


        elements.GameMenu.setSpacing(MENU_PADDING);
        elements.NewGame = new Button("新游戏");
        elements.NewGame.setOnAction(event -> {
            try {
                MenuController.initGame(stage,elements,TypeOfInit.General);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        elements.GameMenu.getChildren().add(elements.NewGame);
        elements.LoadFromSave = new Button("加载残局");
        elements.LoadFromSave.setOnAction(event -> {
            try {
                MenuController.initGame(stage,elements,TypeOfInit.FromSave);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        elements.GameMenu.getChildren().add(elements.LoadFromSave);


        elements.BoardSurface = new Pane();

        elements.GameRoot.getChildren().add(elements.ChessBoard);
        elements.GameRoot.getChildren().add(elements.PlayerRed);
        elements.GameRoot.getChildren().add(elements.PlayerBlack);
        elements.GameRoot.getChildren().add(elements.GamePane);

        elements.PaneChuhe=new Pane();
        elements.PaneHanjie=new Pane();
        elements.Chuhe=new Label("楚河");
        elements.Hanjie=new Label("汉界");
        //String StyleOfMiddleBoard = new String();

        elements.ChessBoard.getChildren().add(elements.PaneChuhe);
        elements.ChessBoard.getChildren().add(elements.PaneHanjie);
        elements.PaneChuhe.getChildren().add(elements.Chuhe);
        elements.PaneHanjie.getChildren().add(elements.Hanjie);
        elements.ChessBoard.getChildren().add(elements.BoardSurface);
        elements.PaneChuhe.setMouseTransparent(true);
        elements.PaneHanjie.setMouseTransparent(true);

        elements.BoardSurface.setOnMouseMoved(mouseEvent -> {
            int Y = Math.toIntExact(Math.round(mouseEvent.getX() / GridWidth))-1;
            int X = Math.toIntExact(Math.round(mouseEvent.getY() / GridWidth))-1;
            double x = mouseEvent.getY();
            double y = mouseEvent.getX();

            if(((Y+1)*GridWidth-y)*((Y+1)*GridWidth-y)+
               ((X+1)*GridWidth-x)*((X+1)*GridWidth-x)<=
                    (0.4*GridWidth)*(0.4*GridWidth)){
                elements.game.getBoard().setHoverPosition((new Position(X,Y)));
                if(HoverChangedFlag){
                    try {
                        refreshWindow(elements);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.out.printf("Hovering (%d, %d)\n",X,Y);
                    HoverChangedFlag=false;
                }
            }
                else elements.game.getBoard().deHover();

        });
    }

    static void refreshWindow(GraphicElements elements) throws Exception {
        double BoardWidth;
        double BoardHeight;
        elements.GameRoot.setPrefSize(elements.WindowRoot.getWidth(),elements.WindowRoot.getHeight());
        BoardWidth = elements.GameRoot.getWidth()*0.6;
        BoardHeight = BoardWidth / ConstantValues.BOARD_RATIO;
        if(BoardHeight > elements.GameRoot.getHeight()){
            BoardHeight = elements.GameRoot.getHeight();
            BoardWidth = BoardHeight * ConstantValues.BOARD_RATIO;
        }

        elements.ChessBoard.setPrefSize(BoardWidth, BoardHeight);
        // 居中
        elements.ChessBoard.setLayoutX((elements.GameRoot.getWidth() - BoardWidth) / 2);
        elements.ChessBoard.setLayoutY((elements.GameRoot.getHeight() - BoardHeight) / 2);

        elements.PlayerBlack.setPrefSize((elements.GameRoot.getWidth()- BoardWidth)/2, BoardHeight/2);
        elements.PlayerRed.setPrefSize((elements.GameRoot.getWidth()- BoardWidth)/2, BoardHeight/2);
        elements.GamePane.setPrefSize((elements.GameRoot.getWidth()- BoardWidth)/2, BoardHeight);

        elements.PlayerBlack.setLayoutX(0);
        elements.PlayerRed.setLayoutX(0);
        elements.GamePane.setLayoutX(elements.GameRoot.getWidth()-(elements.GameRoot.getWidth()- BoardWidth)/2);

        elements.PlayerBlack.setLayoutY((elements.GameRoot.getHeight() - BoardHeight) / 2);
        elements.PlayerRed.setLayoutY((elements.GameRoot.getHeight() - BoardHeight) / 2 + BoardHeight/2);
        elements.GamePane.setLayoutY((elements.GameRoot.getHeight() - BoardHeight) / 2);

        elements.bLabel.setMaxWidth((elements.GameRoot.getWidth()- BoardWidth)/2-2*ConstantValues.MENU_PADDING);
        elements.rLabel.setMaxWidth((elements.GameRoot.getWidth()- BoardWidth)/2-2*ConstantValues.MENU_PADDING);
        elements.gLabel.setMaxWidth((elements.GameRoot.getWidth()- BoardWidth)/2-2*ConstantValues.MENU_PADDING);


        //画棋盘
        GridWidth = BoardWidth/10;
        RenderBoard.drawBoard(elements,GridWidth);

        //画棋子
        elements.BoardSurface.getChildren().clear();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                var u = elements.game.getBoard().getPieceAt(new Position(i,j));
                if(u!=null){
                    RenderBoard.drawPiece(elements,i,j,u.pieceType,GridWidth,u.side);
                }
            }
        }

        //画指示器
        if(elements.game.getBoard().getSelectedPosition()!=null){
            for (var u : elements.game.getBoard().getPieceAt(elements.game.getBoard().getSelectedPosition()).getLegalMoves(elements.game.getBoard(),elements.game.getBoard().getSelectedPosition())){
                RenderBoard.drawMoveIndicator(elements,u.getRow(),u.getCol(),GridWidth);
            }
        }
    }
}
