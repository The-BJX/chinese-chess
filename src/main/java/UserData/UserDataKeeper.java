package UserData;

import java.io.*;
import java.util.ArrayList;

public class UserDataKeeper {
    private ArrayList<User> staticField;
    private String CurrentLoggedIn;
    public UserDataKeeper(){
        staticField = new ArrayList<User>();
        CurrentLoggedIn = null;
    }
    public void saveField(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("UserDataSaves.dat"))){
            oos.writeObject(staticField);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void saveLogState(String curname){//保存登录态
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("CurrentUser.dat"))){
            oos.writeObject(curname);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void loadField(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("UserDataSaves.dat"))){
            staticField = (ArrayList<User>) ois.readObject();
        }catch (FileNotFoundException f){
            saveField();
        }catch(IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public String loadLogState(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("CurrentUser.dat"))){
            String ans = (String) ois.readObject();
            if(ans==null){
                return new String("");
            }else return ans;
        }catch (FileNotFoundException f){
            saveLogState(new String(""));
        }catch(IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean containMd5(String md5){
        for (User u:staticField){
            if(u.hash.equals(md5))return true;
        }return false;
    }
    public boolean containName(String name){
        for (User u:staticField){
            if(name.equals(u.getName()))return true;
        }return false;
    }
    public void addMd5(String name, String md5){
        if(!containMd5(md5))
            staticField.add(new User(name,md5));
        saveField();
    }

    public static void main(){
        UserData.UserDataKeeper u = new UserDataKeeper();
        u.saveField();
    }
}

class User implements Serializable{
    String name;
    String hash;

    public User(String name, String hash) {
        this.name = name;
        this.hash = hash;
    }

    public String getName() {
        return name;
    }
}
