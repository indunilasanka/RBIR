package aztec.rbir_backend.clustering;


/**
 * Created by asankai on 03/08/2017.
 */

//Encoder with term Frequency Inverse document frequency
public class TfIdfEncoder implements Encoder {

    // number of features to be used in feature vector
    private final int numFeatures;
    // inverse document frequency used for normalization of feature vectors
    private Vector inverseDocumentFrequency = null;

    public TfIdfEncoder(int numFeatures) {
        this.numFeatures = numFeatures;
    }

    // Calculate word histogram for document
    private void calcHistogram(Document document) {

        String[] words = document.getPreprocessedContent().split("[^\\w]+");

        Vector histogram = new Vector(numFeatures);

        for (int i = 0; i < words.length; i++) {
            int hashCode = hashWord(words[i]);
            histogram.increment(hashCode);
        }

        histogram.logFrequency();
        document.setHistogram(histogram);
    }

    //calculate histogram for all documents
    private void calcHistogram(DocumentsList documentList) {
        for (Document document : documentList) {
            calcHistogram(document);
        }
    }

    //calculate IDF
    private void calcInverseDocumentFrequency(DocumentsList documentList) {

        inverseDocumentFrequency = new Vector(numFeatures);
        for (Document document : documentList) {
            for (int i = 0; i < numFeatures; i++) {
                if (document.getHistogram().get(i) > 0) {
                    inverseDocumentFrequency.increment(i);
                }
            }
        }

        inverseDocumentFrequency.invert();
        inverseDocumentFrequency.multiply(documentList.size());
        inverseDocumentFrequency.log();
    }

    //encode document with TF-IDF
    private void encode(Document document) {

        // Normalize word histogram by maximum word frequency
        Vector vector = document.getHistogram();
        // Allow histogram to be deallocated as it is no longer needed
        document.setHistogram(null);
        //vector.divide(vector.max());
        // Normalize by inverseDocumentFrequency
        //vector.multiply(inverseDocumentFrequency);
        // Store feature vecotr in document
        document.setVector(vector);
        // Precalculate norm for use in distance calculations
        document.setNorm(vector.magnitude());

    }

    //Hash the word to normalize
    private int hashWord(String word) {

        return Math.abs(word.hashCode()) % numFeatures;
    }

    //encode all documents
    public void encode(DocumentsList documentList) {

        calcHistogram(documentList);
        //calcInverseDocumentFrequency(documentList);
        for (Document document : documentList) {
            encode(document);
        }

    }
}
