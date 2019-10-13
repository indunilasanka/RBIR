package aztec.rbir_database;

import java.util.ArrayList;
import aztec.rbir_database.Entities.Document;
import aztec.rbir_database.Entities.Keyword;
import aztec.rbir_database.dataacess.Data;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.out.println("Maven + Hibernate + MySQL");
        Data data = new Data();
        
        Document doc = new Document();
        
        doc.setName("fyp");
        doc.setPath("c:/");
        doc.setType("pdf");
        
        ArrayList<Keyword> keywords = new ArrayList<Keyword>();
        
        Keyword keyword =  new Keyword();
        keyword.setName("education");
        keywords.add(keyword);
        
        Keyword keyword1 =  new Keyword();
        keyword.setName("free");
        keywords.add(keyword1);
        
        Keyword keyword2 =  new Keyword();
        keyword.setName("higher");
        keywords.add(keyword2);
        
        data.insert(doc, keywords);
    }
}
