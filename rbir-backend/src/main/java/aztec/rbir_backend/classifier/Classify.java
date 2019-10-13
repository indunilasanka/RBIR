package aztec.rbir_backend.classifier;


/**
 * Created by asankai on 15/06/2017.
 */
public class Classify {
    public static String classify(String content){
        String category = Classifier.getCategory(content);
        return "success";
    }
}
