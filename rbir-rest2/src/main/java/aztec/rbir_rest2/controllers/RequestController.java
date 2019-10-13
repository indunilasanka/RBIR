package aztec.rbir_rest2.controllers;

import java.io.File;
import java.util.*;

import aztec.rbir_backend.document.Document;
import aztec.rbir_rest2.models.DocumentModel;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import aztec.rbir_database.Entities.PublicUser;
import aztec.rbir_database.Entities.Request;
import aztec.rbir_database.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import aztec.rbir_backend.email.MailClient;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

@RestController
@RequestMapping("/request")
public class RequestController {

	@Autowired
	MailClient mc;

	@Autowired
	RequestService rqs;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/public-request", method = RequestMethod.POST)
	public
	@ResponseBody
	ResponseEntity<Set<DocumentModel>> handlePublicRequest(@RequestBody Map<String, String> message){

		Set<SearchHit> results = Document.freeTextSearch(message.get("request"), "security_level_1");

		if(results.size() > 0) {
			Set<DocumentModel> result = new HashSet<DocumentModel>();

			for (SearchHit hit : results) {
				Text[] summary = hit.getHighlightFields().get("content").fragments();
				ArrayList<String> content = new ArrayList<String>();
				for (Text text : summary) {
					content.add(text.toString());
				}

				DocumentModel resultDoc = new DocumentModel(hit.getId(), hit.getSource().get("name").toString(), content, hit.getSource().get("type").toString(), hit.getSource().get("category").toString());
				System.out.println(hit.getSource().get("path").toString());
				File file = new File(hit.getSource().get("path").toString());
				resultDoc.setFile(file);
				result.add(resultDoc);
			}

			return new ResponseEntity<Set<DocumentModel>>(result, HttpStatus.OK);
		}

		PublicUser pUser;
		
		pUser= rqs.checkUserofEmail(message.get("email"));
		
		if(pUser==null){
			
			pUser = new PublicUser();
			pUser.setUsername(message.get("username"));
			pUser.setEmail(message.get("email"));
			pUser.setReputation(1);
			pUser.setImage(message.get("image_url"));
			rqs.saveUser(pUser);

		}else{
			
			int currentReputation = pUser.getReputation();
			pUser.setReputation(currentReputation+1);

		}

		Request req = new Request();
		req.setpUser(pUser);
		req.setRequest(message.get("request"));
		req.setState("pending");
		rqs.saveRequest(req);

		return null;
	
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PreAuthorize("hasAuthority('ADMIN_USER')")
	@RequestMapping(value = "/get-requests", method = RequestMethod.GET)
	@ResponseBody
	public List<Request> getRequests(){
		
		List<Request> requests =  rqs.getRequests();
		return requests;
		
	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/delete-request", method = RequestMethod.POST)
	@ResponseBody
	public void deleteRequest(@RequestParam("email") String email,@RequestParam("request") String req, @RequestParam("requestid") int reqId){
		
		rqs.deleteRequest(reqId);
  		try {
 			mc.generateAndSendRejectEmail(email, req);
 		} catch (AddressException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (MessagingException e) {
     		// TODO Auto-generated catch block
 			e.printStackTrace();
    	}

		
	}
	
	
}
