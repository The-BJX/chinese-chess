package chinese_chess;

import GameDialogues.GameDialogue;
import UserData.UserDataKeeper;
import data.GameStatus;
import data.Position;
import data.Side;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
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

        elements.Dialogue=new GameDialogue();

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
        elements.bLabel = new Label("黑方");
        elements.bLabel.setWrapText(true);
        elements.bLabel.setStyle("-fx-font-size: 20; -fx-text-fill: black;");
        elements.BlackMenu.getChildren().add(elements.bLabel);

        elements.rLabel = new Label("红方");
        elements.rLabel.setWrapText(true);
        elements.rLabel.setStyle("-fx-font-size: 20; -fx-text-fill: red;");
        elements.RedMenu.getChildren().add(elements.rLabel);


        elements.UserLabel = new Label();
        elements.UserLabel.setText("用户：游客");
        elements.UserLabel.setStyle("-fx-text-fill:black; -fx-font-size:16;");
        elements.Username="游客";

        elements.gLabel = new Label("游戏菜单");
        elements.gLabel.setWrapText(true);
        elements.gLabel.setStyle("-fx-font-size: 20; -fx-text-fill: blue;");
        elements.GameMenu.getChildren().add(elements.gLabel);
        elements.GameMenu.getChildren().add(elements.UserLabel);

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
        elements.SaveGame = new Button("保存残局");
        elements.SaveGame.setOnAction(event -> {
            try {
                MenuController.saveGame(elements,stage);
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        });
        elements.GameMenu.getChildren().add(elements.LoadFromSave);
        elements.GameMenu.getChildren().add(elements.SaveGame);

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
                    (0.5*GridWidth)*(0.5*GridWidth)){
                elements.game.getBoard().setHoverPosition((new Position(X,Y)));
                if(HoverChangedFlag){
                    try {
                        refreshWindow(elements);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //System.out.printf("Hovering (%d, %d)\n",X,Y);
                    HoverChangedFlag=false;
                }
            }
            else{
                elements.game.getBoard().deHover();
                if(HoverChangedFlag){
                    //System.out.println("Dehovered");
                    try {
                        refreshWindow(elements);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //System.out.printf("Hovering (%d, %d)\n",X,Y);
                    HoverChangedFlag=false;
                }
            }

        });
        elements.BlackRegret = new Button("悔棋");
        elements.RedRegret = new Button("悔棋");
        elements.BlackMenu.getChildren().add(elements.BlackRegret);
        elements.RedMenu.getChildren().add(elements.RedRegret);
        elements.BlackRegret.setOnAction(event -> {
            try {
                elements.game.getBoard().regretLastMove();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        });
        elements.RedRegret.setOnAction(event -> {
            try {
                elements.game.getBoard().regretLastMove();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        });

        elements.SignIn = new Button("登录");
        elements.GameMenu.getChildren().add(elements.SignIn);
        elements.SignIn.setOnAction(actionEvent -> {
            if(elements.SignIn.getText().equals("登录"))
                elements.Dialogue.startInputDialogue(elements,"登入","输入用户名","user-name",stage,"Username",Side.BLACK);
            else{
                elements.Username=new String("游客");
                elements.SignIn.setText("登录");
                try{GraphicController.refreshWindow(elements);}catch (Exception e){};
            }
        });

        elements.userDataKeeper = new UserDataKeeper();
        elements.userDataKeeper.loadField();
        //elements.userDataKeeper.addMd5("ce8385dfd627955cc2fff43ed2d9372c");
        elements.Register = new Button("注册");
        elements.Register.setOnAction(actionEvent -> {
            elements.Dialogue.startInputDialogue(elements,"注册","输入用户名","username",stage,"RegisterUsername",null);
        });
        elements.GameMenu.getChildren().add(elements.Register);

        elements.isViewingRecord=false;
        elements.ViewRecord = new Button("复盘");
        elements.ViewRecord.setOnAction(e-> {
            try {
                MenuController.handleViewRecordButton(stage,elements);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        elements.GameMenu.getChildren().add(elements.ViewRecord);
        elements.game.isViewingRecord=false;
    }

    public static void refreshWindow(GraphicElements elements) throws Exception {
        if(elements.game.getGameStatus()== GameStatus.ONGOING){
            if(elements.game.getBoard().getCurrentTurn().equals(Side.BLACK)){
                elements.WhosTurn.setText("黑方行棋");
                if(elements.game.isViewingRecord==false){
                    elements.BlackRegret.setDisable(true);
                    elements.RedRegret.setDisable(false);
                }
            }else{
                elements.WhosTurn.setText("红方行棋");
                if(elements.game.isViewingRecord==false){
                    elements.BlackRegret.setDisable(false);
                    elements.RedRegret.setDisable(true);
                }
            }
        }else if(elements.game.getGameStatus()==GameStatus.RED_WIN){
            elements.WhosTurn.setText("红方胜利");
            elements.BlackRegret.setDisable(true);
            elements.RedRegret.setDisable(true);
        }else if(elements.game.getGameStatus()==GameStatus.BLACK_WIN){
            elements.WhosTurn.setText("黑方胜利");
            elements.BlackRegret.setDisable(true);
            elements.RedRegret.setDisable(true);
        }else if(/*在此讨论Stalemate的情况*/false){

        }
        if(elements.game.getBoard().moveHistory.isEmpty()==true){
            elements.BlackRegret.setDisable(true);
            elements.RedRegret.setDisable(true);
        }
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

        elements.UserLabel.setText("用户："+elements.Username);
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
        //上一步起点
        Position prev = elements.game.getBoard().previousStepFrom();
        Position curr = elements.game.getBoard().previousStepTo();
        if(prev!=null){
            RenderBoard.drawPrevPiece(elements,prev.getRow(),prev.getCol(),elements.game.getBoard().getPieceAt(curr).pieceType,GridWidth,elements.game.getBoard().getPieceAt(curr).side);
        }
        //窗口，最后画是为了保持在最上方
        if(elements.Dialogue.isActive()){
            elements.Dialogue.updateDialogue(elements.WindowRoot.getWidth(),elements.WindowRoot.getHeight());
        }

    }
}
