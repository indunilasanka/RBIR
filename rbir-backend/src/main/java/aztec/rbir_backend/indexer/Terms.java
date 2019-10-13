package aztec.rbir_backend.indexer;

/**
 * Created by asankai on 28/05/2017.
 */

import aztec.rbir_backend.logic.FileReaderFactory;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import java.io.IOException;
import java.io.StringReader;


public class Terms {

    public static String getTerms(String filePath) {
        String content = FileReaderFactory.read(filePath);
        if(content == null)
            return "Invalid File Type";
        //content = preprocess(content);
        return content.trim();
    }

    public static String getTermsQuery(String query) {
        String terms = preprocess(query);
        return terms.trim();
    }

    public static String getProcessedContent(String content)
    {
        content = preprocess(content);
        return content.trim();
    }

    public static String preprocess(String content){
        String cleanedStr = content.replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase();

        EnglishAnalyzer analyzer = new EnglishAnalyzer(); //stop words

        TokenStream tokenStream = analyzer.tokenStream("test",new StringReader(cleanedStr.trim()));
        tokenStream = new StopFilter(tokenStream,EnglishAnalyzer.getDefaultStopSet());
       // System.out.println(tokenStream);
        StringBuilder sb = new StringBuilder();
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                sb.append(term + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }



}
