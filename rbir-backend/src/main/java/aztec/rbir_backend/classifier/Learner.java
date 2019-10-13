package aztec.rbir_backend.classifier;

import java.io.*;
import java.util.Random;
import aztec.rbir_backend.globals.Global;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * Created by asankai on 14/06/2017.
 */
public class Learner {
    /**
     * Object that stores training data.
     */
    Instances trainData;
    /**
     * Object that stores the filter
     */
    StringToWordVector filter;
    /**
     * Object that stores the classifier
     */
    FilteredClassifier classifier;

    /**
     * This method loads a dataset in ARFF format. If the file does not exist,
     * or it has a wrong format, the attribute trainData is null.
     *
     * @param fileName
     *            The name of the file that stores the dataset.
     */
    public void loadDataset(InputStream fileName) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileName));
            ArffReader arff = new ArffReader(reader);
            trainData = arff.getData();
            System.out.println("===== Loaded dataset: " + fileName + " =====");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    /**
     * This method evaluates the classifier. As recommended by WEKA
     * documentation, the classifier is defined but not trained yet. Evaluation
     * of previously trained classifiers can lead to unexpected results.
     */
    public void evaluate(int numOfCategories) {
        try {
            trainData.setClassIndex(0);
            filter = new StringToWordVector();
            filter.setAttributeIndices("last");

            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayes());

            Evaluation eval = new Evaluation(trainData);
            int folds = numOfCategories;
            eval.crossValidateModel(classifier, trainData, folds , new Random(1));

            System.out.println("===== Evaluating on filtered (training) dataset =====");
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toClassDetailsString());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem found when evaluating");
        }
    }

    /**
     * This method trains the classifier on the loaded dataset.
     */
    public void learn() {
        try {
            trainData.setClassIndex(0);
            filter = new StringToWordVector();
            filter.setAttributeIndices("last");
            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayes());


            classifier.buildClassifier(trainData);
            // Uncomment to see the classifier
            System.out.println("===== Training on filtered (training) dataset =====");
            //System.out.println(classifier);
        } catch (Exception e) {
            System.out.println("Problem found when training");
        }
    }

    public void saveModel() {

        try {
            File file = new File(Global.path +"naiveClassifier.dat");
            ObjectOutputStream out = new ObjectOutputStream(

                    new FileOutputStream(file));

            out.writeObject(classifier);
            out.close();
            System.out.println("===== Saved model: "  + " =====");
        } catch (IOException e) {
            System.out.println("Problem found when writing: ");
        }
    }

    public void trainModel(int numOfCategories) {

        Learner learner;

        InputStream arffFile = null;


        try {
            arffFile =  new FileInputStream(Global.path+"keys.arff");
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }


        learner = new Learner();
        learner.loadDataset(arffFile);
        // Evaluation mus be done before training
        // More info in:
        // http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
        learner.evaluate(numOfCategories);
        learner.learn();
        learner.saveModel();
    }

}
