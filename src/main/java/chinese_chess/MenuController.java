package chinese_chess;

import Game.Game;
import GameSave.ChineseChessDataSaver;
import data.GameStatus;
import data.Side;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static chinese_chess.ConstantValues.MENU_PADDING;

public class MenuController {
    static void initGame(Stage stage, GraphicElements elements, TypeOfInit type) throws Exception {
        try{
            elements.GameMenu.getChildren().remove(elements.RecordControlMenu);
            if(elements.game.getGameStatus()==GameStatus.ALTERING){
                elements.game.setGameStatus(GameStatus.ONGOING);
            }
        }catch (Exception e){}

        if(type==TypeOfInit.General){
            System.out.println("Starting New Game");
            elements.game=new Game(elements.Username,elements);
            //elements.GameMenu.getChildren().remove(elements.NewGame);
            //elements.GameMenu.getChildren().remove(elements.LoadFromSave);
            if(false==elements.GameMenu.getChildren().contains(elements.WhosTurn))
                elements.GameMenu.getChildren().add(elements.WhosTurn);

            elements.BlackSurrender.setDisable(false);
            elements.BlackAskTie.setDisable(false);
            elements.RedSurrender.setDisable(false);
            elements.RedAskTie.setDisable(false);
            elements.game.setGameStatus(GameStatus.ONGOING);

            GraphicController.refreshWindow(elements);
            /*从头开始一个游戏*/
        }
        else if(type==TypeOfInit.FromSave){
            System.out.println("Starting From Save");
            //elements.GameMenu.getChildren().remove(elements.NewGame);
            //elements.GameMenu.getChildren().remove(elements.LoadFromSave);
            //开个输入框，然后读取文件
            FileChooser filechooser= new FileChooser();
            filechooser.setTitle("选取存档");
            File file = filechooser.showOpenDialog(stage);
            System.out.println(file);
            try{
                if(file!=null){
                    try{
                        elements.game.getBoard().checkBoardFromFile(elements.Username,file.getPath());
                        elements.game=new Game(elements.Username,elements);
                        elements.game.getBoard().loadBoardFromFile(elements.Username,file.getPath());
                    }catch(Exception e){
                        throw e;
                    }
                }
            } catch (Exception e){
                elements.Dialogue.startInfoDialogue(elements,"错误",e.getMessage(),stage);
            }//test
            System.out.println("Loading Save of User "+elements.Username);

            GraphicController.refreshWindow(elements);
        }else if(type==TypeOfInit.ViewRecord){
            System.out.println("复盘开始");
            FileChooser filechooser= new FileChooser();
            filechooser.setTitle("选取存档");
            File file = filechooser.showOpenDialog(stage);
            System.out.println(file);
            try{
                if(file!=null){
                    try{
                        elements.game.getBoard().checkBoardFromFile(elements.Username,file.getPath());
                        elements.game=new Game(elements.Username,elements);
                        elements.game.getBoard().loadBoardFromFile(elements.Username,file.getPath());
                        disableAllPlayerButtons(elements);
                    }catch(Exception e){
                        throw e;
                    }
                }else{
                    return;
                }
            }catch (Exception e){
                elements.Dialogue.startInfoDialogue(elements,"错误",e.getMessage(),stage);
                return;
            }

            //做加载动作
            elements.game.getBoard().returnViewToInitial();
            elements.NextStep = new Button("下一步");
            elements.LastStep = new Button("上一步");
            elements.GotoStart = new Button("回到起点");
            elements.NextStep.setOnAction(actionEvent -> {
                try {
                    handleRecordNextStep(elements);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            elements.LastStep.setOnAction(actionEvent -> {
                try {
                    handleRecordPrevStep(elements);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            elements.GotoStart.setOnAction(actionEvent -> {
                try {
                    handleRecordRestart(elements);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });


            elements.RecordControlMenu = new VBox();
            elements.RecordControlButtons = new HBox();
            elements.RecordControlButtons.getChildren().add(elements.GotoStart);
            elements.RecordControlButtons.getChildren().add(elements.LastStep);
            elements.RecordControlButtons.getChildren().add(elements.NextStep);
            elements.RecordControlMenu.setStyle("-fx-border-color:black;-fx-border-width:2px;");
            elements.RecordControlMenu.getChildren().add(elements.RecordControlButtons);
            elements.CurrentStep = new Label("第0步");
            elements.CurrentStep.setStyle("-fx-font-size: 16; -fx-text-fill: black;");
            elements.RecordControlMenu.getChildren().add(elements.CurrentStep);


            elements.RecordControlMenu.setPadding(new Insets(MENU_PADDING/2,MENU_PADDING/2,MENU_PADDING/2,MENU_PADDING/2));
            elements.GameMenu.getChildren().add(elements.RecordControlMenu);
            elements.game.isViewingRecord=true;

            elements.game.setGameStatus(GameStatus.ALTERING);

        }
    }
    static void saveGame(GraphicElements elements, Stage stage)throws Exception{
        System.out.println("Saving current");
        FileChooser fileChooser = new FileChooser();
        //fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Game Save File","*.dat"));

        fileChooser.setTitle("保存残局");
        File file = fileChooser.showSaveDialog(stage);
        elements.game.getBoard().saveBoard(elements.Username,file.getPath());
    }
    static void handleViewRecordButton(Stage stage, GraphicElements elements) throws Exception {
        disableAllPlayerButtons(elements);
        try {
            MenuController.initGame(stage, elements, TypeOfInit.ViewRecord);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //把几个按钮加上：上一步 下一步 回到起点

        GraphicController.refreshWindow(elements);
    }
    static void handleRecordNextStep(GraphicElements elements) throws Exception {
        elements.game.getBoard().viewNextMove();
        elements.CurrentStep.setText("第"+elements.game.getBoard().currentViewingStep+"步");
        GraphicController.refreshWindow(elements);
        disableAllPlayerButtons(elements);
    }
    static void handleRecordPrevStep(GraphicElements elements) throws Exception {
        elements.game.getBoard().viewPreviousMove();
        elements.CurrentStep.setText("第"+elements.game.getBoard().currentViewingStep+"步");
        GraphicController.refreshWindow(elements);
        disableAllPlayerButtons(elements);
    }
    static void handleRecordRestart(GraphicElements elements) throws Exception {
        elements.game.getBoard().returnViewToInitial();
        elements.CurrentStep.setText("第"+elements.game.getBoard().currentViewingStep+"步");
        GraphicController.refreshWindow(elements);
        disableAllPlayerButtons(elements);
    }

    static void disableAllPlayerButtons(GraphicElements elements) throws Exception {
        for(var u:elements.BlackMenu.getChildren()){
            if(u instanceof Button){
                u.setDisable(true);
            }
        }
        for(var u:elements.RedMenu.getChildren()){
            if(u instanceof Button){
                u.setDisable(true);
            }
        }
        if(elements.game.getBoard().getCurrentTurn().equals(Side.BLACK)){
            elements.WhosTurn.setText("黑方行棋");
            if(elements.game.isViewingRecord==false){
                elements.BlackRegret.setDisable(true);
                elements.RedRegret.setDisable(false);
                elements.BlackAIAssist.setDisable(false);
                elements.RedAIAssist.setDisable(true);
            }
        }else{
            elements.WhosTurn.setText("红方行棋");
            if(elements.game.isViewingRecord==false){
                elements.BlackRegret.setDisable(false);
                elements.RedRegret.setDisable(true);
                elements.BlackAIAssist.setDisable(true);
                elements.RedAIAssist.setDisable(false);
            }
        }
    }
    static void activatePlayerButtons(GraphicElements elements){
        elements.BlackSurrender.setDisable(false);
        elements.RedSurrender.setDisable(false);
        elements.BlackAskTie.setDisable(false);
        elements.RedAskTie.setDisable(false);
        if(elements.game.getBoard().getCurrentTurn().equals(Side.BLACK)){
            elements.WhosTurn.setText("黑方行棋");
            if(elements.game.isViewingRecord==false){
                elements.BlackRegret.setDisable(true);
                elements.RedRegret.setDisable(false);
                elements.BlackAIAssist.setDisable(false);
                elements.RedAIAssist.setDisable(true);
            }
        }else{
            elements.WhosTurn.setText("红方行棋");
            if(elements.game.isViewingRecord==false){
                elements.BlackRegret.setDisable(false);
                elements.RedRegret.setDisable(true);
                elements.BlackAIAssist.setDisable(true);
                elements.RedAIAssist.setDisable(false);
            }
        }
    }
}
