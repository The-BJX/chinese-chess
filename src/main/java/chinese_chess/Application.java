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
    public void start(Stage primaryStage) throws Exception {

        elements.game=new Game(elements.Username);
        GraphicController.initGraphics(primaryStage,elements);

        ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        elements.WindowRoot.widthProperty().addListener(sizeListener);
        elements.WindowRoot.heightProperty().addListener(sizeListener);

        primaryStage.fullScreenProperty().addListener((obs,oldValue,newValue)-> {
            try {
                windowReactToChange();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        primaryStage.maximizedProperty().addListener((obs,oldValue,newValue)-> {
            try {
                windowReactToChange();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



        Scene scene = new Scene(elements.WindowRoot, 1000, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("中国象棋 Chinese-Chess");


        primaryStage.show();
        GraphicController.refreshWindow(elements);
        GraphicController.refreshWindow(elements);
        Platform.runLater(()->{
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }
    void windowReactToChange() throws Exception {
        //真是一坨屎，但是我有什么办法
        GraphicController.refreshWindow(elements);
        GraphicController.refreshWindow(elements);
        Platform.runLater(()->{
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                GraphicController.refreshWindow(elements);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(()->{
                try {
                    GraphicController.refreshWindow(elements);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    GraphicController.refreshWindow(elements);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(()->{
                    try {
                        GraphicController.refreshWindow(elements);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        GraphicController.refreshWindow(elements);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Platform.runLater(()->{
                        try {
                            GraphicController.refreshWindow(elements);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            GraphicController.refreshWindow(elements);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
            });
        });
        GraphicController.refreshWindow(elements);

    }



}
