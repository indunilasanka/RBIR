package aztec.rbir_backend.classifier;

import aztec.rbir_backend.clustering.Document;
import aztec.rbir_backend.clustering.DocumentsList;
import aztec.rbir_backend.globals.Global;
import com.google.common.collect.Collections2;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by asankai on 22/08/2017.
 */
public class TrainingModel {

    public static void calculateTrainingWords(DocumentsList newDocList)
    {
        int numFeatures = 10000;
        Learner learner = new Learner();
        ArrayList<String> finalWordList  = new ArrayList<String>();
        ArrayList<String> categoryList  = new ArrayList<String>();

        TfIdfEncoderClassifier Feature_Matrix_Creator = new TfIdfEncoderClassifier(numFeatures);

        for(Document doc:newDocList)
        {
            if(!categoryList.contains(doc.getCategory()))
            {
                categoryList.add(doc.getCategory());
            }
        }



        File file = new File(Global.path+"keys.arff");

        PrintWriter emptyWriter = null;
        try {
            emptyWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        emptyWriter.print("");

        emptyWriter.append("@relation security_category\n");
        emptyWriter.flush();

        String attributeStr = "@attribute class {";
        for(String category: categoryList){
            attributeStr = attributeStr + category + ",";
        }
        attributeStr = attributeStr.substring(0,attributeStr.length()-1);
        attributeStr+="}\n";

        emptyWriter.append(attributeStr);
        emptyWriter.flush();

        emptyWriter.append("@attribute text String\n@data\n");
        emptyWriter.flush();
        emptyWriter.close();

        //find new words with categories to add to the training word list
        for (String category : categoryList)
        {
            ArrayList<Document> CategorizedList = new ArrayList<>(Collections2.filter(newDocList, doc -> doc.getCategory().equals(category)));
            finalWordList = Feature_Matrix_Creator.encode(CategorizedList);
            finalWordList.replaceAll(word -> "\n"+category+","+"'"+word+"'");
            writeToArffFile(finalWordList);
        }

        System.out.println("Starting to train the naive baysian model");
        learner.trainModel(categoryList.size());
        System.out.println("End of training naive baysian model");

    }

    public static void writeToArffFile(ArrayList<String> dataSet)  {

        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(Global.path+"keys.arff",true));


            for(String str : dataSet)
            {
                writer.append(str);
                writer.flush();
            }
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

/*
    public void InitialProcess()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream arffFile = null;
        try {

            arffFile = classLoader.getResource("keys.arff").openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(arffFile));
            ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
            HashSet<String> keys_set = new HashSet<String>();
            reader.close();

            String arffContent = arff.getData().toString();
            String[] parts = arffContent.split("@data");
            String[] tags = parts[1].split("\n");

            for(int i=0;i<tags.length;i++)
            {
                tags[i]= tags[i].substring(tags[i].lastIndexOf(",") + 1);
                tags[i] = tags[i].replaceAll("[']","");
                keys_set.add(tags[i]);
            }
            write(keys_set);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
