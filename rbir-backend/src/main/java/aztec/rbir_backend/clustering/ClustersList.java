package aztec.rbir_backend.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by asankai on 03/08/2017.
 */

//Clusters list
public class ClustersList extends ArrayList<Cluster> implements Serializable{

    //initialize with number of clusters
    public ClustersList(int initialCapacity) {
        super(initialCapacity);
    }

    //update centroids of all clusters
    public void updateCentroids() {
        for (Cluster cluster : this) {
            cluster.updateCentroid();
        }
    }

    //find furthest document
    public Document findFurthestDocument(Distance distance, DocumentsList documentList) {
        double furthestDistance = Double.MIN_VALUE;
        Document furthestDocument = null;
        for (Document document : documentList) {
            if (!document.isAllocated()) {
                double documentDistance = distance.calcDistance(document, this);
                if (documentDistance > furthestDistance) {
                    furthestDistance = documentDistance;
                    furthestDocument = document;
                }
            }
        }
        return furthestDocument;
    }

    //Used to cleanup at the end of each iteration of k means
    public void emptyClusters() {
        for (Cluster cluster : this) {
            cluster.clear();
        }
    }

    //find nearest cluster to a document
    public Cluster findNearestCluster(Distance distance, Document document) {
        Cluster nearestCluster = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Cluster cluster : this) {
            double clusterDistance = distance.calcDistance(document, cluster);
            if (clusterDistance < nearestDistance) {
                nearestDistance = clusterDistance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }


    //used to optimize number of clusters
    public double calcIntraInterDistanceRatio(Distance distance) {
        if (this.size() > 1) {
            double interDist = calcInterClusterDistance(distance);
            if (interDist > 0.0) {
                return calcIntraClusterDistance(distance) / interDist;
            } else {
                return Double.MAX_VALUE;
            }
        } else {
            return Double.MAX_VALUE;
        }
    }

    //average intra cluster distance
    private double calcIntraClusterDistance(Distance distance) {
        double avgIntraDist = 0.0;
        int numDocuments = 0;
        for (Cluster cluster : this) {
            double clusterIntraDist = 0.0;
            for (Document document : cluster) {
                clusterIntraDist += distance.calcDistance(document, cluster);
            }
            numDocuments += cluster.size();
            avgIntraDist += clusterIntraDist;
        }
        return avgIntraDist / numDocuments;
    }


    //average inter cluster distance
    private double calcInterClusterDistance(Distance distance) {

        if (this.size() > 1) {
            double avgInterDist = 0.0;
            for (Cluster cluster1 : this) {
                for (Cluster cluster2 : this) {
                    if (cluster1 != cluster2) {
                        avgInterDist += distance.calcDistance(cluster1, cluster2);
                    }
                }
            }
            // there are N*N-1 unique pairs of clusters
            avgInterDist /= (this.size() * (this.size() - 1));
            return avgInterDist;
        } else {
            return 0.0;
        }
    }

    //sort clusters list and each cluster
    public void sort() {
        for (Cluster cluster : this) {
            Collections.sort(cluster);
        }
        Collections.sort(this);
    }


    public String toString() {
        sort();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.size(); i++) {
            sb.append(this.get(i));
            if (i < this.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
