package aztec.rbir_rest2.controllers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import aztec.rbir_backend.clustering.Document;
import aztec.rbir_rest2.models.*;
import aztec.rbir_backend.classifier.*;
import aztec.rbir_backend.clustering.*;
import aztec.rbir_backend.globals.Global;
import com.google.common.collect.Collections2;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    private String fileDir = Global.path + "indexedFiles"; //uncomment for local server
    //private String fileDir = "indexedFiles"; //uncomment for hosted server

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<Set<DocumentModel>> list(@RequestParam("query") String query,@RequestParam("checked") String checked, @RequestParam("level") String secLevel) {
        System.out.println("Query " + query);

        Set<SearchHit> res = null;

        if(checked.equals("true")) {
            res = aztec.rbir_backend.document.Document.phraseTextSearch(query,secLevel);
        }
        else {
            res = aztec.rbir_backend.document.Document.freeTextSearch(query,secLevel);
        }

        Set<DocumentModel> result = new HashSet<DocumentModel>();

        for(SearchHit hit : res){
            Text[] summary = hit.getHighlightFields().get("content").fragments();

            ArrayList<String> content = new ArrayList<String>();
            for(Text text: summary){
                content.add(text.toString());
            }

            DocumentModel resultDoc = new DocumentModel(hit.getId(),hit.getSource().get("name").toString(),content,hit.getSource().get("type").toString(),hit.getSource().get("category").toString());
            System.out.println(hit.getSource().get("path").toString());
            File file = new File(hit.getSource().get("path").toString());
            resultDoc.setFile(file);
            result.add(resultDoc);
        }
        return new ResponseEntity<Set<DocumentModel>>(result, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<SetupResponse> handleFileUpload(@RequestParam("file") ArrayList<MultipartFile> files) {

        String result = null;
        DocumentsList documentList = new DocumentsList(files);

        Encoder encoder = new TfIdfEncoder(10000);
        encoder.encode(documentList);

        if(Global.getClassificationAlgo() == "Naive"){
            documentList.forEach(e -> {
                    String  predictedCategory = Classifier.getCategory(e.getPreprocessedContent());
                    e.setPredictedCategory(predictedCategory);
            });
        }

        else{
            ClustersList clustersList = KMeanClusterer.loadModel();
            Distance distance = new CosineDistance();
            documentList.forEach(e -> {
                String  predictedCategory = KMeanClusterer.findCluster(e,clustersList,distance);
                e.setPredictedCategory(predictedCategory);
            });
        }

        BulkProcessor bulkProcessor = aztec.rbir_backend.document.Document.getBulkProcessor();

        documentList.forEach(e -> {
            File file = new File(e.getFilePath());
            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            //File destinationDir = new File(Global.path+dir+"/"+e.getPredictedCategory()+"/"); //uncomment for hosted server
            File destinationDir = new File(dir+"/"+e.getPredictedCategory()+"/"); //uncomment for local server
            try {
                System.out.println("Test");
                Map document = new HashMap<String, Object>();
                document.put("name",file.getName());
                document.put("type",e.getType());
                document.put("path",destinationDir.getCanonicalPath() + "\\" + file.getName());
                document.put("content",e.getContent());
                document.put("category", e.getPredictedCategory());
                bulkProcessor.add(new IndexRequest(e.getPredictedCategory(),"document",e.getId()+"").source(document));
               // aztec.rbir_backend.document.Document.create(document,e.getCategory());
                FileUtils.moveFileToDirectory(file, destinationDir, true);
            } catch (IOException e1) {
                e1.printStackTrace();

            }
        });

      /*  try {
            bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        ArrayList<Map<String, String>> docCat = new ArrayList<Map<String, String>>();

        documentList.forEach(e->{
            Map<String,String> doc_cat = new HashMap<String,String>();
            doc_cat.put("category",e.getPredictedCategory());
            doc_cat.put("document",e.getTitle());
        });

        ArrayList<Map<String,String>> numDocCategory = new ArrayList<Map<String, String>>();

        for (String category: Global.getCategories()){
            Map<String, String> num_doc_category = new HashMap<String, String>();
            num_doc_category.put("category",category);
            num_doc_category.put("size", aztec.rbir_backend.document.Document.getNumDocs(category)+"");
            numDocCategory.add(num_doc_category);
        }

        SetupResponse response = new SetupResponse();
        response.setSuccess(true);
        response.setDoc_category(docCat);
        response.setDocToatl(aztec.rbir_backend.document.Document.getNumDocs("_all"));
        response.setNum_doc_category(numDocCategory);

        System.out.println("test");

        return new ResponseEntity<SetupResponse>(response, HttpStatus.OK);
    }



    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/setup", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<SetupResponse> handleInitialSetup(@RequestParam("file") ArrayList<MultipartFile> files, @RequestParam("level") ArrayList<String> categories, @RequestParam("securitylvls") ArrayList<String> levels)
    {
        Global.setCategories(levels);

        DocumentsList documentList1 = new DocumentsList(files, categories);
        DocumentsList documentList2 = new DocumentsList();

        for(aztec.rbir_backend.clustering.Document doc: documentList1){
            documentList2.add(new Document(doc));
        }

        Encoder encoder = new TfIdfEncoder(10000);
        encoder.encode(documentList1);
        Distance distance = new CosineDistance();
        Clusterer clusterer = new KMeanClusterer(distance, 0.6, 1000);
        ClustersList clusterList = clusterer.cluster(documentList1,3);
        KMeanClusterer.saveModel(clusterList);

        TrainingModel.calculateTrainingWords(documentList2);
        documentList2.forEach(e -> {
            String predictedCategory = Classifier.getCategory(e.getPreprocessedContent());
            e.setPredictedCategory(predictedCategory);
        });


        System.out.println( "----------main thread--------");
        System.out.println( "----------main thread--------");
        System.out.println( "----------main thread--------");

        int numCorrectClassification = 0;

        for(aztec.rbir_backend.clustering.Document doc : documentList1){
            System.out.println(doc.getCategory() + "  " + doc.getPredictedCategory());
            if(doc.getCategory().equals(doc.getPredictedCategory())) {
                numCorrectClassification++;
            }
        }

        int clusteringAccuracy = numCorrectClassification*100/files.size();

        numCorrectClassification = 0;

        System.out.println();

        for(aztec.rbir_backend.clustering.Document doc : documentList2){
            System.out.println(doc.getCategory() + "  " + doc.getPredictedCategory());
            if(doc.getCategory().equals(doc.getPredictedCategory())) {
                numCorrectClassification++;
            }
        }

        int classificationAccuracy = numCorrectClassification*100/files.size();

        DocumentsList indexingDocList = null;

        if(classificationAccuracy >= clusteringAccuracy){
            Global.setClassificationAlgo("Naive");
            indexingDocList = documentList2;
        }
        else {
            Global.setClassificationAlgo("KMeans");
            indexingDocList = documentList1;
        }

        Global.writeToFile();

        levels.forEach(e->{
            aztec.rbir_backend.document.Document.setAnalysisSettings(e);
        });

        BulkProcessor bulkProcessor = aztec.rbir_backend.document.Document.getBulkProcessor();

        ArrayList<Map<String,String>> docCategory = new ArrayList<Map<String,String>>();

        indexingDocList.forEach(e -> {
            Map<String, String> doc_category = new HashMap<>();
            doc_category.put("category",e.getPredictedCategory());
            doc_category.put("document",e.getTitle());
            docCategory.add(doc_category);
            System.out.println(e.getFilePath());
            File file = new File(e.getFilePath());

            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdir();
            }

            //File destinationDir = new File(Global.path+dir+"/"+e.getPredictedCategory()+"/"); //uncomment for hosted server
            File destinationDir = new File(dir+"/"+e.getPredictedCategory()+"/"); //uncomment for local server
            try {
                Map document = new HashMap<String, Object>();
                document.put("name",e.getTitle());
                document.put("type",e.getType());
                document.put("path",destinationDir.getCanonicalPath() + "\\" + file.getName());
                document.put("content",e.getContent());
                document.put("category", e.getPredictedCategory());
                bulkProcessor.add(new IndexRequest(e.getPredictedCategory(),"document",e.getId()+"").source(document));
                FileUtils.moveFileToDirectory(file, destinationDir, true);
            } catch (IOException e1) {
                file.delete();
                e1.printStackTrace();
            }
        });

        try {
            bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, Integer> classifyAccuracy = new HashedMap<String, Integer>();
        classifyAccuracy.put("NaiveBaysian", classificationAccuracy);
        classifyAccuracy.put("KMeans", clusteringAccuracy);


        ArrayList<Map<String,String>> numDocCategory = new ArrayList<Map<String, String>>();


        for (String category: levels){
            Map<String, String> num_doc_category = new HashMap<String, String>();
            num_doc_category.put("category",category);
            num_doc_category.put("size", Collections2.filter(indexingDocList, doc -> doc.getCategory().equals(category)).size()+"");
            numDocCategory.add(num_doc_category);
        }


        SetupResponse response = new SetupResponse();
        response.setSuccess(true);
        response.setClassifier_accuracy(classifyAccuracy);
        response.setDoc_category(docCategory);
        response.setNum_doc_category(numDocCategory);
        response.setDocToatl(indexingDocList.size());

        return new ResponseEntity<SetupResponse>(response, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<SetupResponse> getDetails() {
        SetupResponse response = new SetupResponse();

        response.setSuccess(true);

        ArrayList<Map<String,String>> numDocCategory = new ArrayList<Map<String, String>>();

        for (String category: Global.getCategories()){
            Map<String, String> num_doc_category = new HashMap<String, String>();
            num_doc_category.put("category",category);
            num_doc_category.put("size", aztec.rbir_backend.document.Document.getNumDocs(category)+"");
            numDocCategory.add(num_doc_category);
        }

        response.setDocToatl(aztec.rbir_backend.document.Document.getNumDocs("_all"));

        return new ResponseEntity<SetupResponse>(response, HttpStatus.OK);
    }

}
