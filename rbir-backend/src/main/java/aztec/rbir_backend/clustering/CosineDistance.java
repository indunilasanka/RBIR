package aztec.rbir_backend.clustering;

/**
 * Created by asankai on 03/08/2017.
 */


//calculate cosine distance
public class CosineDistance extends Distance {

    public double calcDistance(Vector vector1, Vector vector2, double norm1,
                               double norm2) {
        return 1.0 - vector1.dot(vector2)/norm1/norm2;
    }
}
