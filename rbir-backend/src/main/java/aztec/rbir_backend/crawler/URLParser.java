package aztec.rbir_backend.crawler;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by asankai on 10/06/2017.
 */
public class URLParser {

    public void parse() {
        URL resource = URLParser.class.getResource("/urlList/Quantcast-Top-Million.txt");
        File file = null;
        try {
           file = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.matches(".*\\d+.*")){

                }
                System.out.println(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
