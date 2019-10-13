package aztec.rbir_backend.clustering;

/**
 * Created by asankai on 03/08/2017.
 */


//calculate jaccard distance
public class JaccardDdistance extends Distance {
    public double calcDistance(Vector vector1, Vector vector2, double norm1,
                               double norm2) {
        double innerProduct = vector1.dot(vector2);
        return Math.abs(1.0 - innerProduct / (norm1 + norm2 - innerProduct));
    }
}
