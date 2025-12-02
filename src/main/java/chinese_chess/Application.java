package chinese_chess;

import Core.Board;
import Game.Game;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    GraphicElements elements = new GraphicElements();

    @Override
    public void start(Stage primaryStage) {

        elements.game=new Game();
        GraphicController.initGraphics(primaryStage,elements);

        ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> GraphicController.refreshWindow(elements);

        elements.WindowRoot.widthProperty().addListener(sizeListener);
        elements.WindowRoot.heightProperty().addListener(sizeListener);

        primaryStage.fullScreenProperty().addListener((obs,oldValue,newValue)-> windowReactToChange());
        primaryStage.maximizedProperty().addListener((obs,oldValue,newValue)-> windowReactToChange());

        Scene scene = new Scene(elements.WindowRoot, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess");


        primaryStage.show();
        GraphicController.refreshWindow(elements);
        GraphicController.refreshWindow(elements);
        Platform.runLater(()->{
            GraphicController.refreshWindow(elements);
            GraphicController.refreshWindow(elements);

        });
    }
    void windowReactToChange(){
        //真是一坨屎，但是我有什么办法
        GraphicController.refreshWindow(elements);
        GraphicController.refreshWindow(elements);
        Platform.runLater(()->{
            GraphicController.refreshWindow(elements);
            GraphicController.refreshWindow(elements);
            Platform.runLater(()->{
                GraphicController.refreshWindow(elements);
                GraphicController.refreshWindow(elements);
                Platform.runLater(()->{
                    GraphicController.refreshWindow(elements);
                    GraphicController.refreshWindow(elements);
                    Platform.runLater(()->{
                        GraphicController.refreshWindow(elements);
                        GraphicController.refreshWindow(elements);
                    });
                });
            });
        });
        GraphicController.refreshWindow(elements);

    }



}
