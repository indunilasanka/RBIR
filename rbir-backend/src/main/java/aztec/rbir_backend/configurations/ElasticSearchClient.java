package aztec.rbir_backend.configurations;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by asankai on 28/05/2017.
 */
public class ElasticSearchClient {
    private static TransportClient esc = null;
    private static InetAddress hostName;
    private ElasticSearchClient(){
    }

    public static TransportClient getClient(){
        if(esc==null){
            try {
                hostName = InetAddress.getLocalHost();

                esc = new PreBuiltTransportClient(Settings.EMPTY)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return esc;
    }

    public static void shutDown(){
        esc.close();
    }
}
