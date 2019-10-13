package aztec.rbir_rest2.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import aztec.rbir_backend.document.Document;
import aztec.rbir_database.Entities.Request;
import aztec.rbir_rest2.models.DocumentModel;
import aztec.rbir_rest2.models.DocumentsToConfirm;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import aztec.rbir_backend.email.MailClient;
import aztec.rbir_database.Entities.SearchResultToConfirm;
import aztec.rbir_database.service.RequestService;
import aztec.rbir_database.service.ResultService;
import aztec.rbir_database.service.UserDataService;

@RestController
@RequestMapping("/result")
public class ResultController {

	@Autowired
	MailClient mc;
	
	@Autowired
	ResultService rs;
	
	@Autowired
	UserDataService uds;
	
	@Autowired
	RequestService rqs;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public void addResultsToConfirm(@RequestParam("adminUserEmail")String adminUserEmail,@RequestParam("reqId")int reqId,@RequestParam("searchId") String searchId,@RequestParam("securityLevel") String securityLevel){

		
		rs.addResultsToConfirm(adminUserEmail,reqId,searchId, securityLevel);

	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public
	@ResponseBody
	ResponseEntity<List<DocumentsToConfirm>> getResultsToConfirm(@RequestParam("adminUserEmail")String adminUserEmail){

		List<DocumentsToConfirm> documets = new ArrayList<DocumentsToConfirm>();
		List<SearchResultToConfirm> results =  rs.getResults(adminUserEmail);

		results.forEach(result->{
			String id = result.getResultId();
			String securityLevel = result.getSecurityLevel();
			String query = result.getRequest().getRequest();
			Set<SearchHit> searchHits = Document.freeTextSearch(query , securityLevel);
			for(SearchHit hit : searchHits){
				if(hit.getId().equals(id)){
					Text[] summary = hit.getHighlightFields().get("content").fragments();

					ArrayList<String> content = new ArrayList<String>();
					for(Text text: summary){
						content.add(text.toString());
					}

					DocumentModel resultDoc = new DocumentModel(hit.getId(),hit.getSource().get("name").toString(),content,hit.getSource().get("type").toString(),hit.getSource().get("category").toString());
					File file = new File(hit.getSource().get("path").toString());
					resultDoc.setFile(file);
					documets.add(new DocumentsToConfirm(result.getId(),resultDoc));
				}
			}


		});

		return new ResponseEntity<List<DocumentsToConfirm>>(documets, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public String confirmResult(@RequestParam("searchResultId") int searchResultId, @RequestParam("filePath") String filePath){


		SearchResultToConfirm  srtc = rs.getResult(searchResultId);
		rqs.deleteRequest(srtc.getRequest().getRequestId());
		rs.deleteResult(searchResultId);
		System.out.println("Send mail");

		return "Success";

	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public void cancelSeachResultRequest(@RequestParam("searchResultId") int searchResultId){
		SearchResultToConfirm  srtc = rs.getResult(searchResultId);
		rqs.deleteRequest(srtc.getRequest().getRequestId());
		rs.deleteResult(searchResultId);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	@ResponseBody
	public void confirmResult(@RequestParam("searchResultId") int searchResultId){

		SearchResultToConfirm  srtc = rs.getResult(searchResultId);
		Request r = srtc.getRequest();
		r.setState("pending");
        rqs.saveRequest(r);
        rs.deleteResult(searchResultId);


	}
	
	
}
