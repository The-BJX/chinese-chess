package UserData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserDataKeeper {
    private static ArrayList<String> staticField=new ArrayList<String>();
    private static void saveField() throws IOException{
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("UserDataSaves.dat"))){
            oos.writeObject(staticField);
        }
    }
}
