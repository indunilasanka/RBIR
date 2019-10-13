package aztec.rbir_rest2.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by asankai on 06/11/2017.
 */

@JsonSerialize
public class SetupResponse {
    private long docToatl;
    private boolean success;
    private Map<String, Integer> classifier_accuracy;
    private ArrayList<Map<String,String>> num_doc_category;
    private ArrayList<Map<String, String>> doc_category;

    public void setClassifier_accuracy(Map<String, Integer> classifier_accuracy) {
        this.classifier_accuracy = classifier_accuracy;
    }

    public void setNum_doc_category(ArrayList<Map<String,String>> num_doc_category) {
        this.num_doc_category = num_doc_category;
    }

    public Map<String, Integer> getClassifier_accuracy() {
        return classifier_accuracy;
    }

    public ArrayList<Map<String,String>> getNum_doc_category() {
        return num_doc_category;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Map<String, String>> getDoc_category() {
        return doc_category;
    }

    public void setDoc_category(ArrayList<Map<String, String>> doc_category) {
        this.doc_category = doc_category;
    }

    public long getDocToatl() {
        return docToatl;
    }

    public void setDocToatl(long docToatl) {
        this.docToatl = docToatl;
    }
}
