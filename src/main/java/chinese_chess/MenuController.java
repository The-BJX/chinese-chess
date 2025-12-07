package chinese_chess;

import Game.Game;
import GameSave.ChineseChessDataSaver;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MenuController {
    static void initGame(Stage stage, GraphicElements elements, TypeOfInit type) throws Exception {
        if(type==TypeOfInit.General){
            System.out.println("Starting New Game");
            elements.game=new Game();
            elements.WhosTurn.setText("请红方先手");
            //elements.GameMenu.getChildren().remove(elements.NewGame);
            //elements.GameMenu.getChildren().remove(elements.LoadFromSave);
            if(false==elements.GameMenu.getChildren().contains(elements.WhosTurn))
                elements.GameMenu.getChildren().add(elements.WhosTurn);
            GraphicController.refreshWindow(elements);
            /*从头开始一个游戏*/
        }else if(type==TypeOfInit.FromSave){
            System.out.println("Starting From Save");
            //elements.GameMenu.getChildren().remove(elements.NewGame);
            //elements.GameMenu.getChildren().remove(elements.LoadFromSave);
            //开个输入框，然后读取文件
            FileChooser filechooser= new FileChooser();
            filechooser.setTitle("选取存档");
            File file = filechooser.showOpenDialog(stage);
            System.out.println(file);
            elements.game.getBoard().loadBoardFromFile(file.getPath());
            GraphicController.refreshWindow(elements);
        }
    }
    static void saveGame(GraphicElements elements, Stage stage)throws Exception{
        System.out.println("Saving current");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存残局");
        File file = fileChooser.showSaveDialog(stage);
        elements.game.getBoard().saveBoard(file.getPath());
    }
}
