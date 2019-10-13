package aztec.rbir_backend.classifier;

import aztec.rbir_backend.clustering.*;
import aztec.rbir_backend.clustering.Vector;
import aztec.rbir_backend.globals.Global;
import aztec.rbir_backend.logic.WordFrequency;
import java.io.*;
import java.util.*;
import static com.google.common.primitives.Ints.min;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Created by asankai on 03/08/2017.
 */

//Encoder with term Frequency Inverse document frequency
public class TfIdfEncoderClassifier {

    // number of features to be used in feature vector
    private final int numFeatures;
    // inverse document frequency used for normalization of feature vectors
    private Vector inverseDocumentFrequency = null;
    private HashMap<Integer, ArrayList<String>> termFrequencyMap;
    private HashMap<Integer, Double> tfMap;
    private HashMap<Integer, Double> idfMap;

    public TfIdfEncoderClassifier(int numFeatures)
    {
        this.numFeatures = numFeatures;
    }


    // Calculate word histogram for document
    public void calcHistogram(Document document) {

        String[] words = document.getPreprocessedContent().split("[^\\w]+");
        Vector histogram = new Vector(numFeatures);

        termFrequencyMap = new HashMap<Integer, ArrayList<String>>();
        //termFrequencyMap = WordFrequency.getMostFrequentWords(document.getContents());


        for (int i = 0; i < words.length; i++) {
                int hashCode = hashWord(words[i]);
                histogram.increment(hashCode);
        }

        histogram.logFrequency();
        document.setHistogram(histogram);
    }

    //calculate histogram for all documents
    private Vector calcHistogram(ArrayList<Document>  documentList) {
        Vector wholeHistogram = new Vector(numFeatures);
        for (Document document : documentList) {
            Vector vector = document.getHistogram();
            wholeHistogram.plus(vector);
        }
        return wholeHistogram;
    }

    //calculate IDF
    private Vector calcInverseDocumentFrequency(ArrayList<Document>  documentList) {

        inverseDocumentFrequency = new Vector(numFeatures);
        for (Document document : documentList) {
            for (int i = 0; i < numFeatures; i++) {
                if (document.getHistogram().get(i) > 0) {
                    inverseDocumentFrequency.increment(i);
                }
            }
        }

        inverseDocumentFrequency.invert();
        inverseDocumentFrequency.multiply(documentList.size());
        inverseDocumentFrequency.log();

        return inverseDocumentFrequency;
    }

    //encode document with TF-IDF
    private void encode(Document document) {

        // Normalize word histogram by maximum word frequency
        Vector vector = document.getHistogram();
        // Allow histogram to be deallocated as it is no longer needed
        document.setHistogram(null);
        //vector.divide(vector.max());
        // Normalize by inverseDocumentFrequency
        //vector.multiply(inverseDocumentFrequency);
        // Store feature vecotr in document
        document.setVector(vector);
        // Precalculate norm for use in distance calculations
        document.setNorm(vector.magnitude());

    }

    //Hash the word to normalize
    private int hashWord(String word) {

        return Math.abs(word.hashCode()) % numFeatures;
    }

    //encode all documents
    public ArrayList<String> encode(ArrayList<Document>  documentList) {

        Vector wholeHistogram = null;
        Vector inverseDocumentFrequency = null;
        tfMap = new HashMap<Integer, Double>();
        idfMap = new HashMap<Integer, Double>();
        ArrayList<String> finalWordList  = new ArrayList<String>();
       // HashSet<String> keys_set = new HashSet<String>();

      //  keys_set = read();

        for (Document document: documentList){
            calcHistogram(document);
        }

        for (Document document: documentList)
        {
            String[] words = document.getPreprocessedContent().split("[^\\w]+");

            for (int i = 0; i < words.length; i++) {
                int hashCode = hashWord(words[i]);

                if(termFrequencyMap.get(hashCode) == null){
                    ArrayList<String> tokens = new ArrayList<String>();
                    tokens.add(words[i]);
                    termFrequencyMap.put(hashCode, tokens);
                }
                else {
                    termFrequencyMap.get(hashCode).add(words[i]);
                }

            }
        }

        wholeHistogram = calcHistogram(documentList);
        inverseDocumentFrequency = calcInverseDocumentFrequency(documentList);

        for(int i=0; i< min(wholeHistogram.length(),inverseDocumentFrequency.length()); i++)
        {
            if(wholeHistogram.get(i) != 0.0) {
                tfMap.put(i, wholeHistogram.get(i));
                idfMap.put(i, inverseDocumentFrequency.get(i));
            }
        }

        ArrayList<String> tokens = new ArrayList<String>();
        Set<String> tokenSet;

        tfMap = WordFrequency.sortByValue(tfMap);
        for ( int key : tfMap.keySet() ) {
            tokens = termFrequencyMap.get(key);
            tokenSet = new HashSet<>();
            tokenSet.addAll(tokens); // add the arraylist to hashset to remove common terms
          //  tokenSet.removeAll(keys_set); // remove tokens already in the arff file

            for(String token : tokenSet)
            {
                if((!finalWordList.contains(token))&(!NumberUtils.isNumber(token))&(token.length() != 1))
                {
                    finalWordList.add(token);
                  //  keys_set.add(token);
                }
            }

            if(finalWordList.size() >= (documentList.size()*5))
            {
                break;
            }
        }


        idfMap = WordFrequency.sortByValue(idfMap);
        for ( int key : idfMap.keySet() ) {
            tokens = termFrequencyMap.get(key);
            tokenSet = new HashSet<>();
            tokenSet.addAll(tokens);
            tokenSet.addAll(tokens); // add the arraylist to hashset to remove common terms
          //  tokenSet.removeAll(keys_set); // remove tokens already in the arff file

            for(String token : tokenSet)
            {
                if((!finalWordList.contains(token))&(!NumberUtils.isNumber(token))&(token.length() != 1))
                {
                    finalWordList.add(token);
               //     keys_set.add(token);
                }
            }

            if(finalWordList.size() >= (documentList.size()*10))
            {
                break;
            }
        }

       /* try {
            write(keys_set);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return finalWordList;
    }

    public HashSet<String> read () {

        HashSet<String> keys_set = null;
        try {

            ObjectInputStream input = new ObjectInputStream(new FileInputStream(Global.path+"file.bin"));

            keys_set = (HashSet<String>) (input.readObject());
        }
        catch (Exception e) {
            keys_set = new HashSet<String >();
            System.out.println(e);
        }

        return keys_set;
    }

    public void write (HashSet<String> keys_set) throws IOException {
        try {

            File file = new File(Global.path+"file.bin");
            FileOutputStream fos = new FileOutputStream(file);

            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(keys_set);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }



}
