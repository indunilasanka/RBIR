package aztec.rbir_backend.indexer;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by asankai on 10/06/2017.
 */
public class Index {
    public static boolean index(String tokenStream, String docId, Hashtable<String,Hashtable<String, ArrayList<Integer>>> hashtable){

        String[] tokens = tokenStream.split(" ");

        for(String str:tokens){
            if(!hashtable.containsKey(str))
                hashtable.put(str,new Hashtable<String,ArrayList<Integer>>());
            if(hashtable.get(str).get(docId) == null){
                ArrayList<Integer> positions = new ArrayList<Integer>();
                for(int i=0;i<tokens.length;i++){
                    if(str.equals(tokens[i])){
                        positions.add(i);
                    }
                }
                hashtable.get(str).put(docId,positions);
            }
        }
        return true;
    }
}
