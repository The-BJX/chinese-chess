package chinese_chess;

import AIMove.AIMove;
import GameSave.MoveRecord;
import data.GameStatus;
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
        elements.Chuhe.setMouseTransparent(true);
        elements.Hanjie.setMouseTransparent(true);

    }
    public static void drawPiece(GraphicElements elements, int x, int y, PieceType type, double GridWidth, Side side){
        Circle tmp = new Circle();
        tmp.setFill(Color.web("#FFD963"));
        tmp.setRadius(GridWidth*0.4);
        tmp.setStroke(Paint.valueOf("#000000"));
        if (type ==PieceType.GENERAL) {
            for (var u:elements.game.getBoard().getThreatenedPositions(side)){
                if (u.equals(new Position(x,y))){
                    tmp.setStroke(Paint.valueOf("#ff0000"));
                    break;
                }
            }
        }

        if(elements.game.getBoard().getSelectedPosition()!=null&&elements.game.getBoard().getSelectedPosition().equals(new Position(x,y))){
            tmp.setStrokeWidth(6);
        }else{
            tmp.setStrokeWidth(4);
        }
        if(elements.game.getBoard().getHoverPosition()!=null&&elements.game.getBoard().getHoverPosition().equals(new Position(x,y))){
            tmp.setLayoutX((y+1.1)*GridWidth);
            tmp.setLayoutY((x+0.9)*GridWidth);
            drawShadow(elements,x,y,GridWidth);
        }else{
            tmp.setLayoutX((y+1)*GridWidth);
            tmp.setLayoutY((x+1)*GridWidth);
        }
        Label tmplabel;
        tmplabel=getPieceLabel(type,side);

        Font tmpfont = Font.loadFont("file:HZW005.ttf",GridWidth/1.9);


        tmplabel.setPrefWidth(GridWidth);
        tmplabel.setPrefHeight(GridWidth);
        tmplabel.setAlignment(Pos.CENTER);
        tmplabel.setFont(tmpfont);
        if(elements.game.getBoard().getHoverPosition()!=null&&elements.game.getBoard().getHoverPosition().equals(new Position(x,y))){
            tmplabel.setLayoutX((y+0.08)*GridWidth+tmpfont.getSize());
            tmplabel.setLayoutY((x-0.15)*GridWidth+tmpfont.getSize());
        }
        else{
            tmplabel.setLayoutX((y-0.02)*GridWidth+tmpfont.getSize());
            tmplabel.setLayoutY((x-0.05)*GridWidth+tmpfont.getSize());

        }
        tmplabel.setMouseTransparent(true);

        if(side==Side.RED)tmplabel.setTextFill(Color.web("#df0000"));
        if(side==Side.BLACK)tmplabel.setTextFill(Color.BLACK);

        tmp.setOnMouseClicked(event -> {
            if(false==elements.game.isViewingRecord){
                try {
                    elements.game.touchPosition(new Position(x,y));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.printf("Touched piece (%d,%d)\n",x,y);

            }
            if(elements.game.getBoard().getSelectedPosition()!=null)
                System.out.printf("What is now selected: %s\n", elements.game.getBoard().getSelectedPosition().toString());

            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            event.consume();
        });

        elements.BoardSurface.getChildren().add(tmp);
        elements.BoardSurface.getChildren().add(tmplabel);

    }
    public static void drawMoveIndicator(GraphicElements elements, int x, int y, double GridWidth){
        Circle tmp = new Circle();
        //tmp.setFill(Color.web("#EBF6FF"));
        tmp.setFill(Color.web("#A6B2D7"));
        tmp.setOpacity(0.5);
        tmp.setRadius(0.4*GridWidth);
        tmp.setStrokeWidth(0);
        if(elements.game.getBoard().getHoverPosition()!=null&&elements.game.getBoard().getHoverPosition().equals(new Position(x,y))){
            tmp.setLayoutX((y+1.1)*GridWidth);
            tmp.setLayoutY((x+0.9)*GridWidth);
            drawLighterShadow(elements,x,y,GridWidth,true);
        }else{
            tmp.setLayoutX((y+1)*GridWidth);
            tmp.setLayoutY((x+1)*GridWidth);
            drawLighterShadow(elements,x,y,GridWidth,false);
        }
        elements.BoardSurface.getChildren().add(tmp);

        tmp.setOnMouseClicked(event -> {
            event.consume();

            if(elements.game.getGameStatus()==GameStatus.ONGOING||elements.game.getGameStatus()==GameStatus.ALTERING) {
                try {
                    elements.game.touchPosition(new Position(x, y));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.printf("Indicator at (%d,%d) touched\n", x, y);

                try {
                    GraphicController.refreshWindow(elements);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }
    public static void drawShadow(GraphicElements elements, int x, int y, double GridWidth){
        Circle tmp = new Circle();
        tmp.setFill(Color.web("#000000"));
        tmp.setOpacity(0.4);
        tmp.setLayoutX((y+1)*GridWidth);
        tmp.setLayoutY((x+1)*GridWidth);
        tmp.setStrokeWidth(0);
        tmp.setRadius(GridWidth*0.4+1);
        elements.BoardSurface.getChildren().add(tmp);
    }
    public static void drawPrevPiece(GraphicElements elements, int x, int y, PieceType type, double GridWidth, Side side){
        Circle tmp = new Circle();
        tmp.setFill(Color.web("#FFD963"));
        tmp.setRadius(GridWidth*0.4);
        tmp.setOpacity(0.4);
        tmp.setStroke(Paint.valueOf("#000000"));
        tmp.setStrokeWidth(4);
        tmp.setLayoutX((y+1)*GridWidth);
        tmp.setLayoutY((x+1)*GridWidth);
        Label tmplabel;
        tmplabel=getPieceLabel(type,side);
        tmplabel.setOpacity(0.4);
        Font tmpfont = Font.loadFont("file:HZW005.ttf",GridWidth/1.9);
        tmplabel.setPrefWidth(GridWidth);
        tmplabel.setPrefHeight(GridWidth);
        tmplabel.setAlignment(Pos.CENTER);
        tmplabel.setFont(tmpfont);
        tmplabel.setLayoutX((y-0.02)*GridWidth+tmpfont.getSize());
        tmplabel.setLayoutY((x-0.05)*GridWidth+tmpfont.getSize());
        tmplabel.setMouseTransparent(true);
        tmp.setMouseTransparent(true);
        if(side==Side.RED)tmplabel.setTextFill(Color.web("#df0000"));
        if(side==Side.BLACK)tmplabel.setTextFill(Color.BLACK);
        elements.BoardSurface.getChildren().add(tmp);
        elements.BoardSurface.getChildren().add(tmplabel);
    }
    public static void drawLighterShadow(GraphicElements elements, int x, int y, double GridWidth, boolean lifted){
        if(elements.game.getBoard().getPieceAt(new Position(x,y))!=null){
            return;
        }

        Circle tmp = new Circle();
        tmp.setFill(Color.web("#ffffff"));
        tmp.setOpacity(0.4);
        tmp.setLayoutX((y+1)*GridWidth);
        tmp.setLayoutY((x+1)*GridWidth);
        tmp.setStrokeWidth(0);
        tmp.setRadius(GridWidth*0.4);
        if(lifted){
            tmp.setRadius(GridWidth*0.36);
        }
        elements.BoardSurface.getChildren().add(tmp);
    }


    public static Label getPieceLabel(PieceType type, Side side){
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
                if(side==Side.BLACK)tmplabel = new Label("車");
                else tmplabel = new Label("車");
                break;
            case SOLDIER:
                if(side==Side.BLACK)tmplabel = new Label("兵");
                else tmplabel = new Label("卒");
                break;
            case HORSE:
                if(side==Side.BLACK)tmplabel = new Label("馬");
                else tmplabel = new Label("馬");
                break;
            case ELEPHANT:
                if(side==Side.BLACK)tmplabel = new Label("象");
                else tmplabel = new Label("相");
                break;
            case CANNON:
                if(side==Side.BLACK)tmplabel = new Label("炮");
                else tmplabel = new Label("砲");
                break;
            default:
                tmplabel = new Label("");
        }
        return tmplabel;
    }
}
