package chinese_chess;

import data.PieceType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    GraphicElements elements = new GraphicElements();

    @Override
    public void start(Stage primaryStage) {

        GraphicController.initGraphics(elements);

        ChangeListener<Number> sizeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GraphicController.refreshWindow(elements);
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
