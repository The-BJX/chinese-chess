package UserData;

import java.io.*;
import java.util.ArrayList;

public class UserDataKeeper {
    private ArrayList<User> staticField=new ArrayList<User>();
    public void saveField(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("UserDataSaves.dat"))){
            oos.writeObject(staticField);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void loadField(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("UserDataSaves.dat"))){
            staticField = (ArrayList<User>) ois.readObject();
        }catch(IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
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

    public static void main(String[] Args){
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
