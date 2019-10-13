package aztec.rbir_backend.document;

import aztec.rbir_backend.configurations.ElasticSearchClient;
import aztec.rbir_backend.indexer.Terms;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by asankai on 28/05/2017.
 */
public class Document {
    private static  TransportClient client = ElasticSearchClient.getClient();

    public static BulkProcessor getBulkProcessor(){
        TransportClient client = ElasticSearchClient.getClient();

        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {}

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                        System.out.println(response);
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {}
                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(1000, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
        return bulkProcessor;
    }

    public static IndexResponse create(Map document, String category){
        IndexResponse response = client.prepareIndex(category,"document").setSource(document).get();
        return response;
    }

    public static UpdateResponse update(String category, String id){
        UpdateResponse response = null;
        try {
            response  = client.prepareUpdate(category,"document",id).setDoc(jsonBuilder().startObject().field("category",category).endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static DeleteResponse delete(String category, String id){
        DeleteResponse response = client.prepareDelete(category,"document",id).get();
        return response;
    }

    public static Set<SearchHit> freeTextSearch(String query, String category){
        System.out.println("free text query");
        String[] terms = query.toLowerCase().split(" ");

        String tag = "<b class='highlight'>";
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("content").preTags(tag).postTags("</b>").fragmentSize(200);
        SearchResponse response = client.prepareSearch(category)
                .setTypes("document")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termsQuery("content",terms)).highlighter(highlightBuilder)                 // Query
                .get();

        Set<SearchHit> result = new HashSet<SearchHit>();
        for (SearchHit hit : response.getHits()) {
            result.add(hit);
        }

        return result;
    }

    public static Set<SearchHit> phraseTextSearch(String query, String category){
        System.out.println("phrase query test");

        String tag = "<b class='highlight'>";
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("content").preTags(tag).postTags("</b>").fragmentSize(200);

        SearchResponse response = client.prepareSearch(category)
                .setTypes("document")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchPhraseQuery("content", query.toLowerCase())).highlighter(highlightBuilder)             // Query
                .get();

        Set<SearchHit> result = new HashSet<SearchHit>();
        for (SearchHit hit : response.getHits()) {
            result.add(hit);
        }

        return result;
    }

    public static long getNumDocs(String index){
        SearchResponse response = client.prepareSearch(index)
                .setTypes("document").get();
        return response.getHits().totalHits;
    }

    public static void setAnalysisSettings(String securityLevel){
        Settings settings = null;
        try {
            settings = Settings.builder().loadFromSource(jsonBuilder()
                    .startObject()
                    //Add analyzer settings
                    .startObject("analysis")
                    .startObject("filter")
                    .startObject("test_filter_snowball_en")
                    .field("type", "snowball")
                    .field("language", "English")
                    .endObject()
                    .startObject("test_filter_ngram")
                    .field("type", "edgeNGram")
                    .field("min_gram", 2)
                    .field("max_gram", 30)
                    .endObject()
                    .endObject()
                    .startObject("analyzer")
                    .startObject("test")
                    .field("type", "custom")
                    .field("stopwords", "_english_")
                    .field("tokenizer", "standard")
                    .field("filter", new String[]{"lowercase",
                            "porter_stem"})
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject().string()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(securityLevel);
        createIndexRequestBuilder.setSettings(settings);

            createIndexRequestBuilder.addMapping(
                    "{\n" +
                            "    \"document\": {\n" +
                            "      \"properties\": {\n" +
                            "        \"content\": {\n" +
                            "          \"search_analyzer\": \"test\"\n" +
                            "          \"search_quote_analyzer\": \"test\"\n" +
                            "          \"analyzer\": \"test\"\n" +
                            "        }\n" +
                            "      }\n" +
                            "    }\n" +
                            "  }");


        createIndexRequestBuilder.get();

        System.out.println("test");
    }

}
