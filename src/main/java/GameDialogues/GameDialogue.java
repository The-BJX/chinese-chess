package GameDialogues;

import chinese_chess.GraphicController;
import chinese_chess.GraphicElements;
import data.Side;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GameDialogue {
    String title;
    boolean showing;
    Label TitleLabel;
    Pane BackgroundPane;
    TextField InputField;
    Button FinishButton;
    Label QuestionLabel;
    Button CancelButton;
    
    double widthConstant;
    double heightConstant;
    
    public boolean isActive(){
        return showing;
    }
    public void setActive(){
        showing=true;
    }
    public void shutDown(){
        showing=false;
    }

    public GameDialogue() {
        this.heightConstant = -1;
        this.widthConstant = -1;
    }

    public void startInputDialogue(GraphicElements elements, String title, String question, String default_text, Stage stage, String typeOfDialogue, Side side){
        //生成一个简易输入窗口
        setActive();
        BackgroundPane = new Pane();
        BackgroundPane.setLayoutX(0);
        BackgroundPane.setLayoutY(0);
        BackgroundPane.setPrefSize(stage.getMaxWidth(),stage.getMaxHeight());
        BackgroundPane.setStyle("-fx-background-color: #FFFFFF;");
        BackgroundPane.setOpacity(0.98);

        QuestionLabel = new Label(question);
        QuestionLabel.setStyle("-fx-font-size: 20; -fx-text-fill: black;");
        widthConstant=140;
        heightConstant=40;

        QuestionLabel.setLayoutX(stage.getMaxWidth()/2-widthConstant/2);
        QuestionLabel.setLayoutY(stage.getMaxHeight()*0.35-heightConstant/2);



        TitleLabel = new Label(title);
        TitleLabel.setStyle("-fx-font-size: 40; -fx-text-fill: black;");
        TitleLabel.setLayoutX(stage.getMaxWidth()/2-widthConstant/2);
        TitleLabel.setLayoutY(stage.getMaxHeight()*0.3-heightConstant/2);


        InputField = new TextField();
        InputField.setPromptText(default_text);
        InputField.setLayoutX(stage.getMaxWidth()/2-widthConstant/2);
        InputField.setLayoutY(stage.getMaxHeight()*0.5-heightConstant/2);
        InputField.setPrefSize(2*widthConstant,heightConstant);

        FinishButton = new Button("确认");
        FinishButton.setLayoutX(stage.getMaxWidth()/2-widthConstant/2);
        FinishButton.setLayoutY(stage.getMaxHeight()*0.7-heightConstant/2);
        FinishButton.setPrefSize(2*widthConstant,heightConstant);

        CancelButton = new Button("取消");
        CancelButton.setLayoutX(stage.getMaxWidth()/2-widthConstant/2);
        CancelButton.setLayoutY(stage.getMaxHeight()*0.7-heightConstant/2);
        CancelButton.setPrefSize(2*widthConstant,heightConstant);

        FinishButton.setOnAction(actionEvent -> {
            if(typeOfDialogue.equals("Username")){
                elements.usernameCache=InputField.getText();
                killDialogue(elements);
                startInputDialogue(elements,"登入","请输入密码","password",stage,"Password",side);

            }else if(typeOfDialogue.equals("Password")){
                elements.passwordCache=InputField.getText();
                //检验是否密码正确：
                //把用户名和密码的字符串加起来取哈希，然后文件比对
                String hash = new String("");
                try{
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update((elements.usernameCache+"GenshinImpact"+elements.passwordCache).getBytes());
                    hash = new BigInteger(1,md.digest()).toString(16);
                    System.out.printf("md5: %s\n",hash);
                }catch (NoSuchAlgorithmException e){e.printStackTrace();}
                //安全的不行，亲妈都认不出来
                //然后进行文件比对
                if(elements.userDataKeeper.containMd5(hash)){
                    System.out.println("登陆有效");
                    elements.Username=elements.usernameCache;
                    elements.SignIn.setText("注销");
                    killDialogue(elements);

                }else{
                    System.out.println("账号或密码错误");
                    QuestionLabel.setText("账号或密码错误");
                }

                //正确则执行
            }else if(typeOfDialogue.equals("RegisterUsername")){
                elements.usernameCache=InputField.getText();
                if(elements.userDataKeeper.containName(elements.usernameCache)){
                    System.out.println("早已注册");
                    QuestionLabel.setText("错误：账号已注册");
                }else {
                    killDialogue(elements);
                    startInputDialogue(elements, "注册", "请输入密码", "password", stage, "RegisterPassword", null);
                }
            }else if(typeOfDialogue.equals("RegisterPassword")){
                elements.passwordCache=InputField.getText();
                //检验是否密码正确：
                //把用户名和密码的字符串加起来取哈希，然后文件比对
                String hash = new String("");
                try{
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update((elements.usernameCache+"GenshinImpact"+elements.passwordCache).getBytes());
                    hash = new BigInteger(1,md.digest()).toString(16);
                    System.out.printf("md5: %s\n",hash);
                }catch (NoSuchAlgorithmException e){e.printStackTrace();}
                //安全的不行，亲妈都认不出来
                //然后进行文件比对

                elements.userDataKeeper.addMd5(elements.usernameCache,hash);
                System.out.println("注册成功");
                killDialogue(elements);


            };

            try{GraphicController.refreshWindow(elements);}catch(Exception e){}

        });

        CancelButton.setOnAction(actionEvent -> {
            killDialogue(elements);
        });

        elements.WindowRoot.getChildren().add(BackgroundPane);

        BackgroundPane.getChildren().add(TitleLabel);
        BackgroundPane.getChildren().add(QuestionLabel);
        BackgroundPane.getChildren().add(InputField);
        BackgroundPane.getChildren().add(FinishButton);
        BackgroundPane.getChildren().add(CancelButton);

        BackgroundPane.setMouseTransparent(false);
        try {
            GraphicController.refreshWindow(elements);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void updateDialogue(double width, double height){
        BackgroundPane.setPrefSize(width,height);

        TitleLabel.setLayoutX(width/4-widthConstant/2);
        TitleLabel.setLayoutY(height*0.2-heightConstant/2);

        QuestionLabel.setLayoutX(width/4-widthConstant/2);
        QuestionLabel.setLayoutY(height*0.2+heightConstant*2);

        InputField.setLayoutX(width/4-widthConstant/2);
        InputField.setLayoutY(height*0.2+heightConstant*3.5);
        InputField.setPrefSize(2*widthConstant,heightConstant);

        FinishButton.setLayoutX(width/4-widthConstant/2);
        FinishButton.setLayoutY(height*0.2+heightConstant*5);
        FinishButton.setPrefSize(widthConstant,heightConstant);
        CancelButton.setLayoutX(width/4+widthConstant/2);
        CancelButton.setLayoutY(height*0.2+heightConstant*5);
        CancelButton.setPrefSize(widthConstant,heightConstant);
    }
    private void killDialogue(GraphicElements elements){
        shutDown();
        elements.WindowRoot.getChildren().remove(BackgroundPane);
        try{GraphicController.refreshWindow(elements);}catch (Exception e){}
    }

}
