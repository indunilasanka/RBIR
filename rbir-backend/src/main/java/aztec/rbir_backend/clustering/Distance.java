package aztec.rbir_backend.clustering;

/**
 * Created by asankai on 03/08/2017.
 */


//distance calculation
public abstract class Distance {

    public double calcDistance(Document document, Cluster cluster) {

        return calcDistance(document.getVector(), cluster.getCentroid(),
                document.getNorm(), cluster.getCentroidNorm());

    }


    //minimum distance between a document and cluster centroids
    public double calcDistance(Document document, ClustersList clusterList) {
        double distance = Double.MAX_VALUE;
        for (Cluster cluster : clusterList) {
            distance = Math.min(distance, calcDistance(document, cluster));
        }
        return distance;
    }

    public double calcDistance(Cluster cluster1, Cluster cluster2) {
        return calcDistance(cluster1.getCentroid(), cluster2.getCentroid(),
                cluster1.getCentroidNorm(), cluster2.getCentroidNorm());
    }

    public abstract double calcDistance(Vector vector1, Vector vector2,
                                        double norm1, double norm2);

}
