package aztec.rbir_backend.clustering;

import java.util.ArrayList;

/**
 * Created by asankai on 03/08/2017.
 */


//Cluster of documents
public class Cluster extends ArrayList<Document> implements Comparable<Cluster>{

    // cluster centroid
    private Vector centroid;
    // norm of cluster centroid
    private double centroidNorm;
    //cluster name
    private final String name;

    //cluster initialization with single document
    public Cluster(Document document) {
        super();
        super.add(document);
        centroid = document.getVector();
        centroidNorm = centroid.magnitude();
        name = document.getCategory();
        document.setPredictedCategory(name);
        document.setIsAllocated();
    }

    //sorting of clusters
    public int compareTo(Cluster cluster) {
        return get(0).compareTo(cluster.get(0));
    }

    //add document to cluster
    public boolean add(Document document) {
        super.add(document);
        centroid.plus(document.getVector());
        centroid.divide(size());
        centroidNorm = centroid.magnitude();
        document.setPredictedCategory(name);
        document.setIsAllocated();
        return true;
    }

    //update centroid of cluster
    public void updateCentroid() {
        centroid = null;
        for (Document document : this) {
            if (centroid == null) {
                centroid = document.getVector();
            } else {
                centroid.plus(document.getVector());
            }
        }
        centroid.divide(size());
        centroidNorm = centroid.magnitude();
    }

    public Vector getCentroid() {
        return centroid;
    }

    public double getCentroidNorm() {
        return centroidNorm;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.size(); i++) {
            sb.append(this.get(i).getId());
            if (i < this.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
