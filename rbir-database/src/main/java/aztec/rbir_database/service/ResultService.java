package aztec.rbir_database.service;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import aztec.rbir_database.Entities.Request;
import aztec.rbir_database.Entities.SearchResultToConfirm;
import aztec.rbir_database.Entities.User;
import aztec.rbir_database.configurations.HibernateUtil;

@Service
public class ResultService {
	
	@Autowired
	UserDataService uds;
	
	@Autowired
	RequestService rqs;

	public List<SearchResultToConfirm> getResults(String adminUserEmail){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<SearchResultToConfirm> resultstoconfirm = null;
		
		try {
			session.beginTransaction();
			
			//String hql = "from SearchResultToConfirm srtc";
			
			//session.createQuery(hql).setFirstResult(0);
			//session.createQuery(hql).setMaxResults(10);
			
			//resultstoconfirm = (List<SearchResultToConfirm>)session.createCriteria(SearchResultToConfirm.class).list();
			
			String hql = "from SearchResultToConfirm srtc where srtc.user.username = :username";
			resultstoconfirm = (List<SearchResultToConfirm>) session.createQuery(hql)
			                    .setString("username", adminUserEmail)
			                    .list();
			
			session.getTransaction().commit();
			session.close();
		}catch(HibernateException e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		
		
		return resultstoconfirm;
	}

	public void addResultsToConfirm(String adminUserEmail, int reqId, String searchId, String securityLevel) {
		
		User user = uds.retrieveFromUserName(adminUserEmail);		
		Request request = rqs.getRequest(reqId);
		request.setState("done");
		
		SearchResultToConfirm searchResult =  new  SearchResultToConfirm();
		searchResult.setUser(user);
		searchResult.setRequest(request);
		searchResult.setResultId(searchId);
		searchResult.setSecurityLevel(securityLevel);
		
        Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			session.beginTransaction();
			session.save(searchResult);
			session.getTransaction().commit();
		}catch(HibernateException e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		
	}
	
    public void deleteResult (int resultId) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
		//Request request = null;
		try {
			session.beginTransaction();			
			String hql = "delete SearchResultToConfirm srtc where srtc.id = :id";
			session.createQuery(hql)
			                    .setInteger("id", resultId)
			                    .executeUpdate();

			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}		
	}


	public SearchResultToConfirm getResult(int resultId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		SearchResultToConfirm resultstoconfirm = null;

		try {
			session.beginTransaction();

			String hql = "from SearchResultToConfirm srtc where srtc.id = :id";
			resultstoconfirm = (SearchResultToConfirm) session.createQuery(hql)
					.setInteger("id", resultId)
					.uniqueResult();

			session.getTransaction().commit();
			session.close();
		}catch(HibernateException e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}


		return resultstoconfirm;
	}


}
