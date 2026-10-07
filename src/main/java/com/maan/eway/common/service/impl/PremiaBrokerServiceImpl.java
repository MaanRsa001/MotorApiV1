package com.maan.eway.common.service.impl;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.common.res.PremiaTiraReq;
import com.maan.eway.common.res.PremiaTiraRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
public class PremiaBrokerServiceImpl {
	
	
	@PersistenceContext
	private EntityManager em;
	
	
	@Value(value = "${PremiaBrokerTiraCode}")
	private String premiaBrokerTiraCode;
	
	@Value(value = "${PremiaBrokerSPCode}")
	private String premiaBrokerSPCode;
	
	@Value(value = "${PremiaBrokerSPCodeAndTiraCode}")
	private String tiSPcode;
	
	Gson json = new Gson();
	
	@Value(value = "${ClaimBasicAuthPass}")
	private String ClaimBasicAuthPass;
	
	@Value(value = "${ClaimBasicAuthName}")
	private String ClaimBasicAuthName;
	
	private Logger log = LogManager.getLogger(PremiaBrokerServiceImpl.class);
	

	public synchronized List<PremiaTiraRes> searchPremiaBrokerTiraCode(PremiaTiraReq req) {
		List<PremiaTiraRes> resList = new ArrayList<PremiaTiraRes>();
		try {
			
			String url = premiaBrokerTiraCode ;
			String auth = ClaimBasicAuthName +":"+ ClaimBasicAuthPass;
			
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")) );
	        String authHeader = "Basic " + new String( encodedAuth );
	     	RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization",authHeader);
			HttpEntity<Object> entityReq = new HttpEntity<Object>(req, headers);
	
			log.info("Api Url -----------> " +  url );
		    log.info("Request -----------> " + json.toJson(req) );
			ResponseEntity<Object> response = restTemplate.postForEntity(url, entityReq, Object.class);
			 
			if(response !=null && response.getBody()!=null ) {
				//	System.out.println(response.getBody());
				log.info("Response -----------> " + json.toJson(response.getBody()) );
				ObjectMapper mapper = new ObjectMapper();
				resList = mapper.convertValue(response.getBody() ,new TypeReference<List<PremiaTiraRes>>(){});
				//resList = (List<PremiaTiraRes>) response.getBody() ;
			}
			
		}catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is --->"+e.getMessage());
				return null;
				}
		return resList;
	}
	
	public synchronized List<PremiaTiraRes> searchPremiaBrokerSpCode(PremiaTiraReq req) {
		List<PremiaTiraRes> resList = new ArrayList<PremiaTiraRes>();
		try {
			
			String url = premiaBrokerSPCode ;
			String auth = ClaimBasicAuthName +":"+ ClaimBasicAuthPass;
			
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")) );
	        String authHeader = "Basic " + new String( encodedAuth );
	     	RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization",authHeader);
			HttpEntity<Object> entityReq = new HttpEntity<Object>(req, headers);

			log.info("Api Url -----------> " +  url );
		    log.info("Request -----------> " + json.toJson(req) );
			ResponseEntity<Object> response = restTemplate.postForEntity(url, entityReq, Object.class);
			System.out.println(response.getBody());
			log.info("Response -----------> " + json.toJson(response.getBody()) );
			
			if(response !=null && response.getBody()!=null ) {
				//	System.out.println(response.getBody());
				log.info("Response -----------> " + json.toJson(response.getBody()) );
				ObjectMapper mapper = new ObjectMapper();
				resList = mapper.convertValue(response.getBody() ,new TypeReference<List<PremiaTiraRes>>(){});
				//resList = (List<PremiaTiraRes>) response.getBody() ;
			}
			
		}catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is --->"+e.getMessage());
				return null;
				}
 		return resList;
	}
	
	public synchronized List<ListItemValue> getSourceTypeDropdown(String insuranceId , String branchCode, String itemType) {
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);
			
			//Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));
			
			
			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
			Predicate b3 = cb.equal(c.get("branchCode"),ocpm1.get("branchCode"));
			Predicate b4 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1,a2,b3,b4);
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
			Predicate b1 = cb.equal(c.get("branchCode"),ocpm2.get("branchCode"));
			Predicate b2 = cb.equal(c.get("companyId"),ocpm2.get("companyId"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3,a4,b1,b2);
						
			// Where
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
			Predicate n5 = cb.equal(c.get("companyId"), "99999");
			Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
			Predicate n8 = cb.or(n4,n5);
			Predicate n9 = cb.or(n6,n7);
			Predicate n10 = cb.equal(c.get("itemType"),itemType );
			query.where(n2,n3,n4,n8,n9,n10).orderBy(orderList);
			
		
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return list ;
	}

	public List<PremiaTiraRes> searchPremiaBrokerTiraCodeAndSalePoint(PremiaTiraReq req) {
		List<PremiaTiraRes> resList = new ArrayList<PremiaTiraRes>();
		try {
			
			String url = tiSPcode ;
			String auth = ClaimBasicAuthName +":"+ ClaimBasicAuthPass;
			
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")) );
	        String authHeader = "Basic " + new String( encodedAuth );
	     	RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization",authHeader);
			HttpEntity<Object> entityReq = new HttpEntity<Object>(req, headers);
	
			log.info("Api Url -----------> " +  url );
		    log.info("Request -----------> " + json.toJson(req) );
			ResponseEntity<Object> response = restTemplate.postForEntity(url, entityReq, Object.class);
			 
			if(response !=null && response.getBody()!=null ) {
				//	System.out.println(response.getBody());
				log.info("Response -----------> " + json.toJson(response.getBody()) );
				ObjectMapper mapper = new ObjectMapper();
				resList = mapper.convertValue(response.getBody() ,new TypeReference<List<PremiaTiraRes>>(){});
				//resList = (List<PremiaTiraRes>) response.getBody() ;
			}
			
		}catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is --->"+e.getMessage());
				return null;
				}
		return resList;
	}
}
