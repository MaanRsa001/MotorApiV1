package com.maan.eway.common.service.impl;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.bean.SeqArchId;
import com.maan.eway.bean.SeqRefno;
import com.maan.eway.common.req.SequenceGenerateReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.SequenceGenerateRes;
import com.maan.eway.repository.SeqArchIdRepository;
import com.maan.eway.repository.SeqOnetimetableRepository;
import com.maan.eway.repository.SeqRefnoRepository;

@Service
public class GenerateSeqNoServiceImpl {
	
	private Logger log = LogManager.getLogger(GenerateSeqNoServiceImpl.class);
	
	@Autowired
	private SeqRefnoRepository refNoRepo ;
	
	@Autowired
	private SeqOnetimetableRepository oneNoRepo ;
	
	@Autowired
	private SeqArchIdRepository archIdRepo ;
	
	@Value(value = "${EwayBasicAuthPass}")
	private String EwayBasicAuthPass;
	
	@Value(value = "${EwayBasicAuthName}")
	private String EwayBasicAuthName;
	
	@Value(value = "${SequenceGenerateUrl}")
	private String SequenceGenerateUrl;

	 public synchronized String generateRefNo() {
	       try {
	    	   SeqRefno entity;
	            entity = refNoRepo.save(new SeqRefno());          
	            return String.format("%05d",entity.getRequestReferenceNo()) ;
	        } catch (Exception e) {
				e.printStackTrace();
				log.info( "Exception is ---> " + e.getMessage());
	            return null;
	        }
	       
	 }
	 
	 public synchronized String generateArchId() {
	       try {
	    	   SeqArchId entity;
	            entity = archIdRepo.save(new SeqArchId());          
	            return String.format("%05d",entity.getArchId()) ;
	        } catch (Exception e) {
				e.printStackTrace();
				log.info( "Exception is ---> " + e.getMessage());
	            return null;
	        }
	       
	 }
	 
	 
	 public  String generateSeqCall(SequenceGenerateReq req ) {
	       try {
	    	String url = SequenceGenerateUrl;
	   		String auth = EwayBasicAuthName +":"+ EwayBasicAuthPass;
	         byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")) );
	         String authHeader = "Basic " + new String( encodedAuth );
	      
	   		RestTemplate restTemplate = new RestTemplate();
	   		HttpHeaders headers = new HttpHeaders();
	   		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
	   		headers.setContentType(MediaType.APPLICATION_JSON);
	   		 headers.set("Authorization",authHeader);
	   		HttpEntity<SequenceGenerateReq> entityReq = new HttpEntity<SequenceGenerateReq>(req, headers);

	   		ResponseEntity<CommonRes> response = restTemplate.postForEntity(url, entityReq, CommonRes.class);
	   		ObjectMapper mapper = new ObjectMapper();
	   		SequenceGenerateRes res = mapper.convertValue(response.getBody().getCommonResponse() ,new TypeReference<SequenceGenerateRes>(){});

	   		String seq = res.getGeneratedValue();
	   		System.out.println("Generated Sequence --> " + seq );
	    	 return seq ;
	        } catch (Exception e) {
				e.printStackTrace();
				log.info( "Exception is ---> " + e.getMessage());
	            return null;
	        }
	       
	 }
}
