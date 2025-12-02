package chinese_chess;

import data.PieceType;
import data.Position;
import data.Side;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class RenderBoard {
    public static void drawBoard(GraphicElements elements, double GridWidth){
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

    public static void drawPiece(GraphicElements elements, int x, int y, PieceType type, double GridWidth, Side side){
        Circle tmp = new Circle();
        tmp.setFill(Color.web("#FFD963"));
        tmp.setRadius(GridWidth*0.4);
        tmp.setLayoutX((y+1)*GridWidth);
        tmp.setLayoutY((x+1)*GridWidth);
        tmp.setStroke(Paint.valueOf("#000000"));
        if(elements.game.getBoard().getSelectedPosition()!=null&&elements.game.getBoard().getSelectedPosition().equals(new Position(x,y))){
            tmp.setStrokeWidth(6);
        }else{
            tmp.setStrokeWidth(4);
        }
        Label tmplabel;
        switch (type){
            case GENERAL:
                if(side==Side.BLACK)tmplabel = new Label("將");
                else tmplabel = new Label("帥");
                break;
            case ADVISOR:
                if(side==Side.BLACK)tmplabel = new Label("士");
                else tmplabel = new Label("仕");
                break;
            case CHARIOT:
                if(side==Side.BLACK)tmplabel = new Label("车");
                else tmplabel = new Label("车");
                break;
            case SOLDIER:
                if(side==Side.BLACK)tmplabel = new Label("兵");
                else tmplabel = new Label("卒");
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
        tmplabel.setLayoutX((y+0.5)*GridWidth);
        tmplabel.setLayoutY((x+0.68)*GridWidth);
        tmplabel.setMouseTransparent(true);

        if(side==Side.RED)tmplabel.setTextFill(Color.web("#df0000"));
        if(side==Side.BLACK)tmplabel.setTextFill(Color.BLACK);

        tmp.setOnMouseClicked(event -> {
            elements.game.touchPosition(new Position(x,y));
            event.consume();
        });

        elements.BoardSurface.getChildren().add(tmp);
        elements.BoardSurface.getChildren().add(tmplabel);

    }
}
