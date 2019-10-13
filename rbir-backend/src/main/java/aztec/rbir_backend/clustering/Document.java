package aztec.rbir_backend.clustering;

import java.io.Serializable;

/**
 * Created by asankai on 03/08/2017.
 */

/* class for each document */
public class Document implements Comparable<Document>, Serializable {

    private String title;
    private String type;
    private String content;
    private String preprocessedContent;
    private long id;
    private String category;
    private String filePath;
    private String predictedCategory;
    private String[] metaDataNames;

    // whether document has been allocated to a cluster
    private boolean allocated;
    // document word histogram
    private Vector histogram;

    // encoded document vector (TF-IDF)
    private Vector vector;
    // precalculated document vector norms
    private double norm;

    public Document(Document document){
        id = document.getId();
        title = document.getTitle();
        type = document.getType();
        content = document.getContent();
        preprocessedContent = document.getPreprocessedContent();
        category = document.getCategory();
        filePath = document.getFilePath();
    }

    public Document(long id, String title, String type, String content, String preprocessedContent, String category, String filePath) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.content = content;
        this.preprocessedContent = preprocessedContent;
        this.category = category;
        this.filePath = filePath;
    }


    //setters
    public void setVector(Vector vector) {
        this.vector = vector;
    }
    public void setIsAllocated() {
        allocated = true;
    }
    public void setHistogram(Vector histogram) {
        this.histogram = histogram;
    }
    public void setNorm(double norm) {
        this.norm = norm;
    }
    public void clearIsAllocated() {
        allocated = false;
    }
    public void setCategory(String category){this.category = category;}
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public void setPredictedCategory(String predictedCategory) {
        this.predictedCategory = predictedCategory;
    }

    //getters
    public boolean isAllocated() {
        return allocated;
    }
    public long getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public String getPreprocessedContent(){ return preprocessedContent;}
    public Vector getVector() {
        return vector;
    }
    public Vector getHistogram() {
        return histogram;
    }
    public double getNorm() {
        return norm;
    }
    public String getCategory(){return category;}
    public String getFilePath() {
        return filePath;
    }
    public String getPredictedCategory() {
        return predictedCategory;
    }

    public String getTitle() {
        return title;
    }
    public String getType(){return type;}

    //compare documents ids
    public int compareTo(Document document) {
        if (id > document.getId()) {
            return 1;
        } else if (id < document.getId()) {
            return -1;
        } else {
            return 0;
        }
    }
}
