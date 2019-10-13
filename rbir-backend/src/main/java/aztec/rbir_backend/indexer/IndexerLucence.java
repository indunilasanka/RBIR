package aztec.rbir_backend.indexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Created by asankai on 21/08/2017.
 */
public class IndexerLucence {
    public static final String FIELD_PATH = "path";
    public static final String FIELD_CONTENTS = "contents";
    public static final String FIELD_CATEGORY = "category";

    public static String createIndex(String files_directory, String index_directory, String category){

        Analyzer analyzer = new SimpleAnalyzer();
        Directory indexDirectory = null;
        IndexWriter indexWriter = null;
        try {
            indexDirectory = FSDirectory.open(Paths.get(index_directory));
            IndexWriterConfig conf = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(indexDirectory, conf);

            File dir = new File(files_directory);
            File[] files = dir.listFiles();
            for (File file : files) {
                if(!file.isDirectory()) {
                    Document document = new Document();

                    String path = file.getCanonicalPath();
                    System.out.println("can path : " + path);
                    document.add(new Field(FIELD_PATH, path, TextField.TYPE_STORED));

                    document.add(new Field(FIELD_CATEGORY, category, TextField.TYPE_STORED));

                    FileReader reader = new FileReader(file);
                    document.add(new TextField(FIELD_CONTENTS, reader));

                    indexWriter.addDocument(document);
                }
            }
            indexWriter.close();
            indexDirectory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }

    public static ArrayList<Document> searchIndex(String searchString) {
        File indexedFilesDir = new File("E:/FYPSavingFolder/indexedFiles/");
        File[] directories = indexedFilesDir.listFiles();

        ArrayList<Document> result = new ArrayList<Document>();

        try {
            for(File directory: directories) {
                String index_directory = directory.getCanonicalPath()+"/index/";
                System.out.println("Searching for '" + searchString + "'");
                Directory indexDirectory = FSDirectory.open(Paths.get(index_directory));
                DirectoryReader indexReader = DirectoryReader.open(indexDirectory);

                IndexSearcher indexSearcher = new IndexSearcher(indexReader);

                Analyzer analyzer = new StandardAnalyzer();

                QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);

                Query query = null;

                query = queryParser.parse(searchString);

                ScoreDoc[] hits = indexSearcher.search(query, 1000).scoreDocs;
                for (int i = 0; i < hits.length; i++) {
                    Document hitDoc = indexSearcher.doc(hits[i].doc);
                    result.add(hitDoc);
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }
        return result;

    }
}
