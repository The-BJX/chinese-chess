package chinese_chess;

import AIMove.AIMove;
import Game.Game;
import GameDialogues.GameDialogue;
import GameSave.MoveRecord;
import UserData.UserDataKeeper;
import data.GameStatus;
import data.Position;
import data.Side;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sounds.GameSoundFX;

public class GraphicController {
    private static GameSoundFX soundFX = new GameSoundFX();
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
    static void initGraphics(Stage stage, GraphicElements elements) throws Exception {

        elements.stage = stage;
        //先读取登录态再开始游戏
        elements.userDataKeeper = new UserDataKeeper();
        elements.Username=elements.userDataKeeper.loadLogState();
        elements.game=new Game(elements.Username,elements);


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
            soundFX.playDeselectSound();
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

        elements.BlackMenu.setSpacing(MENU_PADDING);
        elements.RedMenu.setSpacing(MENU_PADDING);

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

        //提取登录态
        elements.UserLabel = new Label();
        if(elements.Username.equals(new String(""))){
            elements.UserLabel.setText("用户：游客");
        }else{
            elements.UserLabel.setText("用户："+elements.Username);
        }
        elements.UserLabel.setStyle("-fx-text-fill:black; -fx-font-size:16;");


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
            if(elements.Username.equals(new String(""))&&elements.game.getGameStatus()!=GameStatus.ALTERING){
                elements.Dialogue.startInfoDialogue(elements,"游客不能加载残局","请先登录",stage);
                return;
            }
            try {
                MenuController.initGame(stage,elements,TypeOfInit.FromSave);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        elements.SaveGame = new Button("保存残局");
        elements.SaveGame.setOnAction(event -> {
            if(elements.Username.equals(new String(""))&&elements.game.getGameStatus()!=GameStatus.ALTERING){
                elements.Dialogue.startInfoDialogue(elements,"游客不能保存残局","请先登录",stage);
                return;
            }
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
            boolean isGuest = elements.Username.equals(new String(""));
            try {
                elements.game.getBoard().regretLastMove(isGuest);
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
            boolean isGuest = elements.Username.equals(new String(""));
            try {
                elements.game.getBoard().regretLastMove(isGuest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        });
        elements.BlackSurrender = new Button("投降");
        elements.RedSurrender = new Button("投降");
        elements.BlackMenu.getChildren().add(elements.BlackSurrender);
        elements.RedMenu.getChildren().add(elements.RedSurrender);
        elements.BlackSurrender.setOnAction(actionEvent -> {
            if(elements.game.getGameStatus()==GameStatus.ONGOING) {
                elements.Dialogue.startInfoDialogue(elements,"投降","红方胜利",stage);
                elements.game.setGameStatus(GameStatus.RED_WIN);
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        elements.RedSurrender.setOnAction(actionEvent -> {
            if(elements.game.getGameStatus()==GameStatus.ONGOING) {
                elements.Dialogue.startInfoDialogue(elements, "投降", "黑方胜利", stage);
                elements.game.setGameStatus(GameStatus.BLACK_WIN);
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        elements.BlackAskTie = new Button("求和");
        elements.RedAskTie = new Button("求和");
        elements.BlackMenu.getChildren().add(elements.BlackAskTie);
        elements.RedMenu.getChildren().add(elements.RedAskTie);
        elements.BlackAskTie.setOnAction(actionEvent -> {
            if(elements.game.getGameStatus().equals(GameStatus.ONGOING)) {
                elements.Dialogue.startTieDialogue(elements, stage);
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        elements.RedAskTie.setOnAction(actionEvent -> {
            if(elements.game.getGameStatus().equals(GameStatus.ONGOING))
                elements.Dialogue.startTieDialogue(elements,stage);
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        elements.BlackAIAssist = new Button("机器代下");
        elements.RedAIAssist = new Button("机器代下");
        elements.BlackMenu.getChildren().add(elements.BlackAIAssist);
        elements.RedMenu.getChildren().add(elements.RedAIAssist);
        elements.BlackAIAssist.setOnAction(actionEvent -> {
            if(elements.game.getGameStatus().equals(GameStatus.ONGOING)) {
                MoveRecord move = null;
                try {
                    move = elements.aiMove.findBestMove(elements.game.getBoard(),elements.game.getBoard().getCurrentTurn());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    elements.game.touchPosition(move.fromPosition);
                    elements.game.touchPosition(move.toPosition);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        elements.RedAIAssist.setOnAction(actionEvent -> {
            if(elements.game.getGameStatus().equals(GameStatus.ONGOING)) {
                MoveRecord move = null;
                try {
                    move = elements.aiMove.findBestMove(elements.game.getBoard(),elements.game.getBoard().getCurrentTurn());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    elements.game.touchPosition(move.fromPosition);
                    elements.game.touchPosition(move.toPosition);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        elements.SignIn = new Button();
        if(elements.Username.equals(new String(""))){
            elements.SignIn.setText("登录");
        }else {
            elements.SignIn.setText("注销");
        }
        elements.GameMenu.getChildren().add(elements.SignIn);
        elements.SignIn.setOnAction(actionEvent -> {
            if(elements.SignIn.getText().equals("登录"))
                elements.Dialogue.startInputDialogue(elements,"登入","输入用户名","user-name",stage,"Username",Side.BLACK);
            else{
                elements.Username=new String("");
                elements.userDataKeeper.saveLogState(new String(""));
                elements.SignIn.setText("登录");
                try{GraphicController.refreshWindow(elements);}catch (Exception e){};
            }
        });

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
            if(elements.Username.equals(new String(""))&&elements.game.getGameStatus()!=GameStatus.ALTERING){
                elements.Dialogue.startInfoDialogue(elements,"游客不能复盘","请先登录",stage);
                return;
            }
            try {
                MenuController.handleViewRecordButton(stage,elements);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        elements.GameMenu.getChildren().add(elements.ViewRecord);
        elements.game.isViewingRecord=false;
        elements.ChessFont = Font.loadFont("file:HZW005.ttf",20);

        elements.aiMove = new AIMove(2);

        elements.Altermode = new Button("摆棋");
        elements.GameMenu.getChildren().add(elements.Altermode);
        elements.Altermode.setOnAction(actionEvent -> {
            if(elements.game.getGameStatus()==GameStatus.ALTERING){
                elements.game.setGameStatus(GameStatus.ONGOING);
                elements.Altermode.setText("摆棋");
            }else{
                elements.game.setGameStatus(GameStatus.ALTERING);
                elements.Altermode.setText("退出摆棋模式");
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        elements.DifficultyChoice = new ComboBox<String>();
        elements.DifficultyChoice.getItems().addAll("1 - 草履虫","2 - 蛇鼠","3 - 人类","4 - 柯洁(长考)","5 - 邪神(一年下一步)","6 - 上帝(别等，相信我)");
        elements.DifficultyChoice.setValue("人机难度（默认草履虫）");
        elements.aiMove.setMaxDepth(1);
        elements.GameMenu.getChildren().add(elements.DifficultyChoice);
        elements.DifficultyChoice.valueProperty().addListener(change -> {
            int curdiff = Integer.parseInt(String.valueOf(elements.DifficultyChoice.getValue().charAt(0)));
            elements.aiMove.setMaxDepth(curdiff);
            System.out.println(curdiff);
        });



    }

    public static void refreshWindow(GraphicElements elements) throws Exception {
        if(elements.game.getGameStatus()== GameStatus.ONGOING){
            elements.Altermode.setText("摆棋");
            MenuController.activatePlayerButtons(elements);
        }else if(elements.game.getGameStatus()==GameStatus.RED_WIN||elements.game.getGameStatus()==GameStatus.RED_WIN_STALE){
            elements.WhosTurn.setText("红方胜利");
            MenuController.disableAllPlayerButtons(elements);
        }else if(elements.game.getGameStatus()==GameStatus.BLACK_WIN||elements.game.getGameStatus()==GameStatus.BLACK_WIN_STALE){
            elements.WhosTurn.setText("黑方胜利");
            MenuController.disableAllPlayerButtons(elements);
        }else if(elements.game.getGameStatus()==GameStatus.TIE){
            elements.WhosTurn.setText("和棋");
            MenuController.disableAllPlayerButtons(elements);
        }else if(elements.game.getGameStatus()==GameStatus.ALTERING){
            elements.Altermode.setText("退出摆棋模式");
            MenuController.disableAllPlayerButtons(elements);
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

        elements.DifficultyChoice.setPrefWidth(0.8*(elements.GameRoot.getWidth()- BoardWidth)/2);

        if(elements.Username.equals(new String(""))){
            elements.UserLabel.setText("用户：游客");
        }else {
            elements.UserLabel.setText("用户：" + elements.Username);
        }
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

        if(elements.game.getGameStatus()==GameStatus.ALTERING){
            for (int i = 0; i <=9 ; i++) {
                for (int j = 0; j <= 8; j++) {
                    if(elements.game.getBoard().getSelectedPosition()!=null&&elements.game.getBoard().getSelectedPosition().equals(new Position(i,j))==false)
                        RenderBoard.drawMoveIndicator(elements,i,j,GridWidth);
                }
            }
        }else{
            if(elements.game.getBoard().getSelectedPosition()!=null){
                for (var u : elements.game.getBoard().getPieceAt(elements.game.getBoard().getSelectedPosition()).getLegalMoves(elements.game.getBoard(),elements.game.getBoard().getSelectedPosition())){
                    RenderBoard.drawMoveIndicator(elements,u.getRow(),u.getCol(),GridWidth);
                }
            }
        }


        //上一步起点
        Position prev = elements.game.getBoard().previousStepFrom();
        Position curr = elements.game.getBoard().previousStepTo();
        if(prev!=null){
            RenderBoard.drawPrevPiece(elements,prev.getRow(),prev.getCol(),elements.game.getBoard().getPieceAt(curr).pieceType,GridWidth,elements.game.getBoard().getPieceAt(curr).side);
        }

//        if(elements.game.getGameStatus()==GameStatus.ONGOING){
//            RenderBoard.drawRecommendedMove(elements,GridWidth);
//        }
//落子提示。该功能已废除

        //窗口，最后画是为了保持在最上方
        if(elements.Dialogue.isActive()){
            elements.Dialogue.updateDialogue(elements.WindowRoot.getWidth(),elements.WindowRoot.getHeight());
        }

    }
}
