package aztec.rbir_backend.logic;

import aztec.rbir_backend.configurations.ElasticSearchClient;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asankai on 28/05/2017.
 */
public class FileReaderAndIndexer {
    public String indexDocument(String company,String filePath){
        String fileName = new File(filePath).getName();
        String fileType = fileName.substring(fileName.indexOf('.')+1);
        fileName = fileName.substring(0, fileName.indexOf('.'));

        String content = FileReaderFactory.read(filePath);

        String[] frequentWords = WordFrequency.getMostFrequentWords(content,10);

        Map<String,Object> myMap = new HashMap<String,Object>();

        myMap.put("name",fileName);
        myMap.put("file_type",fileType);
        myMap.put("file_path",filePath);
        myMap.put("entered_date",new Date());
        myMap.put("keys",frequentWords);

        TransportClient client = ElasticSearchClient.getClient();
        IndexResponse response = client.prepareIndex(company, "document")
                .setSource(myMap)
                .get();
        System.out.println(response);
        return response.getResult().toString();
    }

}
