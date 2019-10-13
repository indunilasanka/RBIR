package aztec.rbir_backend.clustering;

/**
 * Created by asankai on 03/08/2017.
 */


public abstract class Clusterer {

    // distance measure to use when evaluating distance between documents
    Distance distance;

    public abstract ClustersList cluster(DocumentsList documentList, int numClusters);
}
