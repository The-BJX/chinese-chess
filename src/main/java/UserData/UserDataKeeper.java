package UserData;

import java.io.*;
import java.util.ArrayList;

public class UserDataKeeper {
    private ArrayList<String> staticField=new ArrayList<String>();
    public void saveField(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("UserDataSaves.dat"))){
            oos.writeObject(staticField);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void loadField(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("UserDataSaves.dat"))){
            staticField = (ArrayList<String>) ois.readObject();
        }catch(IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public boolean containMd5(String md5){
        for (var u:staticField){
            if(u.equals(md5))return true;
        }return false;
    }
    public void addMd5(String md5){
        if(!containMd5(md5))
            staticField.add(md5);
        saveField();
    }
}
