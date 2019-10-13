package aztec.rbir_backend.logic;

import aztec.rbir_backend.configurations.ElasticSearchClient;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Iterator;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * Created by asankai on 29/05/2017.
 */
public class DocumentSeeker {
    public ArrayList<String> searchDocument(String company,String[] keys){
        for (String val: keys)
            System.out.println(val);
        ArrayList<String> result = new ArrayList<String>();
        TransportClient client = ElasticSearchClient.getClient();
        QueryBuilder qb = termsQuery(
                "keys",
                keys
        );

        SearchResponse response = client.prepareSearch(company).setTypes("document").setQuery(qb).get();
        System.out.println(response);

        Iterator iterator = response.getHits().iterator();
        while (iterator.hasNext()){
            SearchHit hit = (SearchHit) iterator.next();
            result.add(hit.getSourceAsMap().get("name").toString()+"."+hit.getSourceAsMap().get("file_type").toString());
        }

        System.out.println(result);
        return result;

    }
}
