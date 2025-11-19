package chinese_chess;

import data.PieceType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pieces.Piece;

import java.security.cert.TrustAnchor;

public class Application extends javafx.application.Application {
    private double BOARD_RATIO = 1/1.1;
    private double MENU_PADDING = 10;
    GraphicElements elements = new GraphicElements();

    @Override
    public void start(Stage primaryStage) {

        initGraphics();

        ChangeListener<Number> sizeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                refreshWindow();
            }
        };

        elements.WindowRoot.widthProperty().addListener(sizeListener);
        elements.WindowRoot.heightProperty().addListener(sizeListener);

        primaryStage.fullScreenProperty().addListener((obs,oldValue,newValue)->{
            windowReactToChange();
        });
        primaryStage.maximizedProperty().addListener((obs,oldValue,newValue)->{
            windowReactToChange();
        });

        Scene scene = new Scene(elements.WindowRoot, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess");
        primaryStage.show();
        refreshWindow();
        Platform.runLater(()->{
            refreshWindow();
        });
    }
    void windowReactToChange(){
        //真是一坨屎，但是我有什么办法
        refreshWindow();
        refreshWindow();
        Platform.runLater(()->{
            refreshWindow();
            refreshWindow();
            Platform.runLater(()->{
                refreshWindow();
                refreshWindow();
                Platform.runLater(()->{
                    refreshWindow();
                    refreshWindow();
                    Platform.runLater(()->{
                        refreshWindow();
                        refreshWindow();
                    });
                });
            });
        });
        refreshWindow();
    }

    void refreshWindow(){
        double BoardWidth;
        double BoardHeight;
        elements.GameRoot.setPrefSize(elements.WindowRoot.getWidth(),elements.WindowRoot.getHeight());
        BoardWidth = elements.GameRoot.getWidth()*0.6;
        BoardHeight = BoardWidth / BOARD_RATIO;
        if(BoardHeight > elements.GameRoot.getHeight()){
            BoardHeight = elements.GameRoot.getHeight();
            BoardWidth = BoardHeight *BOARD_RATIO;
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

        elements.bLabel.setMaxWidth((elements.GameRoot.getWidth()- BoardWidth)/2-2*MENU_PADDING);
        elements.rLabel.setMaxWidth((elements.GameRoot.getWidth()- BoardWidth)/2-2*MENU_PADDING);
        elements.gLabel.setMaxWidth((elements.GameRoot.getWidth()- BoardWidth)/2-2*MENU_PADDING);
;


        //画棋盘
        double GridWidth = BoardWidth/10;
        drawBoard(GridWidth);

        //画棋子
        elements.BoardSurface.getChildren().clear();
        drawPiece(0,0,PieceType.GENERAL,GridWidth,Color.BLACK);
    }


    void drawBoard(double GridWidth){
        elements.BoardSurface.setPrefSize(elements.ChessBoard.getWidth(),elements.ChessBoard.getHeight());
        elements.BoardSurface.setLayoutX(0);
        elements.BoardSurface.setLayoutY(0);
        for (int i = 0; i < 9; i++) {
            elements.UpVerticleLine[i].setStartX((1+i)*GridWidth);
            elements.UpVerticleLine[i].setStartY(GridWidth);
            elements.UpVerticleLine[i].setEndX((1+i)*GridWidth);
            elements.UpVerticleLine[i].setEndY(5*GridWidth);
            if(i==0||i==8){
                elements.UpVerticleLine[i].setEndY(6*GridWidth);
            }
        }
        for (int i = 0; i < 9; i++) {
            elements.DownVerticleLine[i].setStartX((1+i)*GridWidth);
            elements.DownVerticleLine[i].setStartY(6*GridWidth);
            elements.DownVerticleLine[i].setEndX((1+i)*GridWidth);
            elements.DownVerticleLine[i].setEndY(10*GridWidth);
        }
        for (int i = 0; i < 10; i++) {
            elements.HorizontalLine[i].setStartX(GridWidth);
            elements.HorizontalLine[i].setStartY(GridWidth*(i+1));
            elements.HorizontalLine[i].setEndX(9*GridWidth);
            elements.HorizontalLine[i].setEndY(GridWidth*(i+1));
        }
        //大营
        elements.CrossLine[0].setStartX(4*GridWidth);
        elements.CrossLine[0].setStartY(GridWidth);
        elements.CrossLine[0].setEndX(6*GridWidth);
        elements.CrossLine[0].setEndY(3*GridWidth);

        elements.CrossLine[1].setEndX(4*GridWidth);
        elements.CrossLine[1].setStartY(GridWidth);
        elements.CrossLine[1].setStartX(6*GridWidth);
        elements.CrossLine[1].setEndY(3*GridWidth);

        elements.CrossLine[2].setStartX(4*GridWidth);
        elements.CrossLine[2].setStartY(8*GridWidth);
        elements.CrossLine[2].setEndX(6*GridWidth);
        elements.CrossLine[2].setEndY(10*GridWidth);

        elements.CrossLine[3].setEndX(4*GridWidth);
        elements.CrossLine[3].setStartY(8*GridWidth);
        elements.CrossLine[3].setStartX(6*GridWidth);
        elements.CrossLine[3].setEndY(10*GridWidth);

        elements.PaneChuhe.setLayoutX(GridWidth);
        elements.PaneChuhe.setLayoutY(5*GridWidth);
        elements.PaneChuhe.setPrefSize(4*GridWidth,GridWidth);

        elements.PaneHanjie.setLayoutX(5*GridWidth);
        elements.PaneHanjie.setLayoutY(5*GridWidth);
        elements.PaneHanjie.setPrefSize(4*GridWidth,GridWidth);

        elements.StyleOfMiddleBoard = String.format("-fx-font-size: %f",0.6*GridWidth);
        elements.Chuhe.setStyle(elements.StyleOfMiddleBoard);
        elements.Hanjie.setStyle(elements.StyleOfMiddleBoard);

        elements.Chuhe.setLayoutX(2*GridWidth-elements.Chuhe.getWidth()/2);
        elements.Chuhe.setLayoutY(GridWidth/2-elements.Chuhe.getHeight()/2);
        elements.Hanjie.setLayoutX(2*GridWidth-elements.Hanjie.getWidth()/2);
        elements.Hanjie.setLayoutY(GridWidth/2-elements.Hanjie.getHeight()/2);

    }
    void initGraphics(){


        elements.WindowRoot = new Pane();
        elements.WindowRoot.setStyle("-fx-background-color: black");


        elements.GameRoot = new Pane();
        elements.GameRoot.setStyle("-fx-background-color: white");
        elements.WindowRoot.getChildren().add(elements.GameRoot);

        // 创建一个固定长宽比的容器
        elements.ChessBoard = new Pane();
        elements.ChessBoard.setStyle("-fx-background-color: burlywood; -fx-border-color: navy;");

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
        elements.gLabel.setWrapText(true);
        elements.GameMenu.getChildren().add(elements.WhosTurn);

        elements.NewGame = new Button("新游戏");
        elements.NewGame.setOnAction(event -> {initGame(TypeOfInit.General);});
        elements.GameMenu.getChildren().add(elements.NewGame);
        elements.LoadFromSave = new Button("加载残局");
        elements.LoadFromSave.setOnAction(event -> {initGame(TypeOfInit.FromSave);});
        elements.GameMenu.getChildren().add(elements.LoadFromSave);


        elements.BoardSurface = new Pane();
        elements.ChessBoard.getChildren().add(elements.BoardSurface);

        elements.GameRoot.getChildren().add(elements.ChessBoard);
        elements.GameRoot.getChildren().add(elements.PlayerRed);
        elements.GameRoot.getChildren().add(elements.PlayerBlack);
        elements.GameRoot.getChildren().add(elements.GamePane);

        elements.PaneChuhe=new Pane();
        elements.PaneHanjie=new Pane();
        elements.Chuhe=new Label("楚河");
        elements.Hanjie=new Label("汉界");
        String StyleOfMiddleBoard = new String();

        elements.ChessBoard.getChildren().add(elements.PaneChuhe);
        elements.ChessBoard.getChildren().add(elements.PaneHanjie);
        elements.PaneChuhe.getChildren().add(elements.Chuhe);
        elements.PaneHanjie.getChildren().add(elements.Hanjie);

    }

    void initGame(TypeOfInit type){
        if(type==TypeOfInit.General){
            System.out.println("Starting New Game");
            elements.GameMenu.getChildren().remove(elements.NewGame);
            elements.GameMenu.getChildren().remove(elements.LoadFromSave);
            elements.WhosTurn.setText("黑方行棋");
        }else if(type==TypeOfInit.FromSave){
            System.out.println("Starting From Save");
            elements.GameMenu.getChildren().remove(elements.NewGame);
            elements.GameMenu.getChildren().remove(elements.LoadFromSave);
        }
    }

    void drawPiece(int x, int y, PieceType type, double GridWidth, Color side){
        Circle tmp = new Circle();
        tmp.setFill(Color.web("#FFD963"));
        tmp.setRadius(GridWidth*0.4);
        tmp.setLayoutX((x+1)*GridWidth);
        tmp.setLayoutY((y+1)*GridWidth);
        tmp.setStroke(Paint.valueOf("#000000"));
        tmp.setStrokeWidth(4);

        Label tmplabel;
        switch (type){
            case GENERAL:
                tmplabel = new Label("将");
                break;

            default:
                tmplabel = new Label("");
        }

        /*for(String u:Font.getFontNames()){
            System.out.println(u);
        }*/

        Font tmpfont = Font.font("华文隶书",GridWidth/1.9);
        //tmpfont.

        tmplabel.setPrefWidth(GridWidth);
        tmplabel.setPrefHeight(GridWidth);
        tmplabel.setAlignment(Pos.CENTER);
        tmplabel.setFont(tmpfont);
        tmplabel.setLayoutX((x+0.5)*GridWidth);
        tmplabel.setLayoutY((y+0.68)*GridWidth);
        tmplabel.setMouseTransparent(true);

        if(side==Color.RED)tmplabel.setTextFill(Color.web("#df0000"));
        if(side==Color.BLACK)tmplabel.setTextFill(Color.BLACK);

        tmp.setOnMouseClicked(event -> {
            System.out.println("Clicked");
        });

        elements.BoardSurface.getChildren().add(tmp);
        elements.BoardSurface.getChildren().add(tmplabel);

    }
}
