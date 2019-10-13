package aztec.rbir_backend.globals;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by asankai on 10/06/2017.
 */
public class Global {
    private static String classificationAlgo;
    private static long lastDocId;
    private static ArrayList<String> categories;


    //public static String path = "/home/rbir/";
    public static String path = "E:/FYP/";


    public Global(){
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(path+"categories.dat"));
                this.categories = (ArrayList<String>) in.readObject();
                System.out.println(categories);
                in.close();
                in = new ObjectInputStream(new FileInputStream(path+"classifierName.dat"));
                this.classificationAlgo = (String) in.readObject();
                System.out.println(classificationAlgo);
                in.close();
                in = new ObjectInputStream(new FileInputStream(path+"lastDocId.dat"));
                this.lastDocId = (long) in.readObject();
                System.out.println(lastDocId);
                in.close();
            } catch (FileNotFoundException e) {
                categories = null;
                this.classificationAlgo = null;
                lastDocId = 0;
            } catch (IOException e) {
                categories = null;
                this.classificationAlgo = null;
                lastDocId = 0;
            } catch (ClassNotFoundException e) {
                categories = null;
                this.classificationAlgo = null;
                lastDocId = 0;
            }
    }

    public static ArrayList<String> getCategories() {
        return categories;
    }

    public static void setCategories(ArrayList<String> categories) {
        Global.categories = categories;
    }

    public static String getClassificationAlgo(){
        return classificationAlgo;
    }

    public synchronized static long getLastDocId(){
        return lastDocId++;
    }

    public static void setClassificationAlgo(String classificationAlgoName){
        classificationAlgo = classificationAlgoName;
    }

    public static void writeToFile(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path+"categories.dat"));
            out.writeObject(categories);
            out.close();
            out = new ObjectOutputStream(new FileOutputStream(path+"classifierName.dat"));
            out.writeObject(classificationAlgo);
            out.close();
            out = new ObjectOutputStream(new FileOutputStream(path+"lastDocId.dat"));
            out.writeObject(lastDocId);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
