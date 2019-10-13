package aztec.rbir_database.service;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import aztec.rbir_database.Entities.PublicUser;
import aztec.rbir_database.Entities.Request;
import aztec.rbir_database.configurations.HibernateUtil;

@Service
public class RequestService {

	
	public PublicUser checkUserofEmail(String email) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
		PublicUser user = null;
		try {
			session.beginTransaction();			
			String hql = "from PublicUser user where user.email = :email";
			user = (PublicUser) session.createQuery(hql)
			                    .setString("email", email)
			                    .uniqueResult();

			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}		
		return user;
	}
	
	
	public void saveRequest(Request req){
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			session.beginTransaction();
			session.saveOrUpdate(req);
			session.getTransaction().commit();
		}catch(HibernateException e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}

	}
	
	public List<Request> getRequests(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Request> requests = null;
		
		try {
			session.beginTransaction();
			
			//String hql = "from Request r where";

			String hql = "from Request r where r.state = 'pending'";
			requests = (List<Request>) session.createQuery(hql)
					.list();
			
			//session.createQuery(hql).setFirstResult(0);
			//session.createQuery(hql).setMaxResults(10);
			
			//requests = (List<Request>)session.createCriteria(Request.class).list();
			
			session.getTransaction().commit();
			session.close();
		}catch(HibernateException e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}
        
		//for(int i=0;i<100000;i++){}
		
		
		return requests;
	}
	
	
    public Request deleteRequest (int requestId) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
		Request request = null;
		try {
			session.beginTransaction();			
			String hql = "delete Request r where r.requestId = :requestId";
			session.createQuery(hql)
			                    .setInteger("requestId", requestId)
			                    .executeUpdate();

			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}		
		return request;
	}
	
    public Request getRequest (int requestId) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
		Request request = null;
		try {
			session.beginTransaction();			
			String hql = "from Request r where r.requestId = :requestId";
			request = (Request) session.createQuery(hql)
			                    .setInteger("requestId", requestId)
			                    .uniqueResult();

			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}		
		return request;
 	}
	
	
	public void saveUser(PublicUser pUser){
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			session.beginTransaction();
			session.save(pUser);
			session.getTransaction().commit();
		}catch(HibernateException e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}

	}
	
	
}
