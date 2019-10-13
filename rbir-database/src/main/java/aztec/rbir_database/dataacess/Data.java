package aztec.rbir_database.dataacess;

import java.util.ArrayList;
import org.hibernate.Session;
import aztec.rbir_database.Entities.Document;
import aztec.rbir_database.Entities.Keyword;
import aztec.rbir_database.Entities.KeywordsInDocument;
import aztec.rbir_database.Entities.User;
import aztec.rbir_database.configurations.HibernateUtil;

public class Data {

	public void insert(Document doc, ArrayList<Keyword> keys){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		session.beginTransaction();
		
		session.save(doc);
		
		User user = new User();
		user = session.get(User.class, (long)4);
		user.setUsername("kelum");
		
		
		
		ArrayList<KeywordsInDocument> kwds =  new ArrayList<KeywordsInDocument>();
		for(Keyword key:keys){
			session.saveOrUpdate(key);
		    KeywordsInDocument kwd =  new KeywordsInDocument();
		    kwd.setDocument(doc);
		    kwd.setKeyword(key);
		    kwds.add(kwd);
		    session.save(kwd);
		}
		session.getTransaction().commit();
	}
	
	public void getKeywordsOfDocument(int docId){
		
	}
	
}
