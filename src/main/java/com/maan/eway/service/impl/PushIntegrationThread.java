package com.maan.eway.service.impl;


import java.net.ConnectException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;


public class PushIntegrationThread implements Runnable
{
	
	
	private Logger log = LogManager.getLogger(PushIntegrationThread.class);
	
    private String quoteNo;
    private List<String> premiaIds;
    private String tokens;
    private String premiaPushLink;

    public PushIntegrationThread(String quoteNo, List<String> premiaIds,String tokens,String premiaPushLink)
    {
        this.quoteNo = quoteNo;
        this.premiaIds = premiaIds;
        this.tokens=tokens;
        this.premiaPushLink=premiaPushLink;
    }

	public void run() {
		PremiaResponse response = new PremiaResponse();

		PremiaRequest request = new PremiaRequest();
		request.setQuoteNo(quoteNo);
		request.setPremiaIds(premiaIds);
		System.out.println((new StringBuilder("PremiaRequest ")).append(request).toString());
		pushPremiaIntegration(request,tokens);
		System.out.println(response);

	}
	public Object pushPremiaIntegration(PremiaRequest premiaReq , String token ) {
	 	Object PremiaRes = null;
		String url = premiaPushLink ;
	try {
		// Frame Tira Req
		System.out.println("Calling : /push/integration/quote");
		RestTemplate temp = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		// header.setCharset("UTF-8");
		header.setBearerAuth(token);
		System.out.println("TOKEN==>" +new Date() + " Start " + token);
		System.out.println("URL==>" +new Date() + " Start " + url);
        System.out.println((new StringBuilder("Req==>")).append(premiaReq).toString());
        System.out.println((new StringBuilder("Json Req==>")).append((new Gson()).toJson(premiaReq)).toString());
		HttpEntity<?> requestent = new HttpEntity<>(premiaReq, header);
		ResponseEntity<Object> postEntity = temp.exchange(url, HttpMethod.POST, requestent,new ParameterizedTypeReference<Object>() {}) ;
		//if(PremiaRes.getStatusCode()==HttpStatus.ACCEPTED) {
		PremiaRes = postEntity.getBody() ;
		//}		
		System.out.println((new StringBuilder("Premia Response==>")).append((new Gson()).toJson(PremiaRes)).toString());
//		System.out.println("Premia Response --> "+PremiaRes);
		System.out.println(new Date() + " End " + url);
	} catch (RestClientException e) {
		if (e.getCause() instanceof ConnectException) {
			System.out.println("Connection refused: Unable to connect to the server at " + url);
		} else {
			System.out.println("An error occurred while making the REST call: " + e.getMessage());
		}
	} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is ---> " + e.getMessage());
	}
	return PremiaRes;
}


}