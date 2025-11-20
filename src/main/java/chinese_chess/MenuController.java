package chinese_chess;

public class MenuController {
    static void initGame(GraphicElements elements,TypeOfInit type){
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
}
