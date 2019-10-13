package aztec.rbir_backend.indexer;


/**
 * Created by asankai on 10/06/2017.
 */
public class Indexer {
    public static String indexFile(String filePath){
        String content = Terms.getTerms(filePath);
        if (content == "Invalid File Type")
            return content;

        // call the below method to expand the training model
        //calculateTrainingWords(content);
        //

        return "fail";
    }

}
