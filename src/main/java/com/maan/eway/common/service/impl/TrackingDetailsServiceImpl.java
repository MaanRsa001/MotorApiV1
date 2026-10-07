package com.maan.eway.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.SeqTrackingId;
import com.maan.eway.bean.TrackingDetails;
import com.maan.eway.common.req.TrackingDetailsSaveReq;
import com.maan.eway.common.res.SuccessRes;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.SeqTrackingRepository;
import com.maan.eway.repository.TrackingDetailsRepository;

@Service
public class TrackingDetailsServiceImpl {
	
	@Autowired
	private TrackingDetailsRepository trackingrepo;
	
	@Autowired
	private ListItemValueRepository livRepo;
	
	@Autowired
	private SeqTrackingRepository seqTracRepo; 


	private Logger log=LogManager.getLogger(TrackingDetailsServiceImpl.class);
	Gson json = new Gson();

	SimpleDateFormat sdfFormat = new SimpleDateFormat("ddMMyyyyhhmmss");


	public	synchronized SuccessRes insertTrackingDetails(TrackingDetailsSaveReq req) {
		SuccessRes res = new SuccessRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		 try {
			 Date today = new Date();
			 
			 TrackingDetails saveData = new TrackingDetails();
			
			 String status = StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus() ;
			 ListItemValue  listItemValue =livRepo.findByItemTypeAndItemCode("TRACKING_STATUS",status);
			 String trackingId =generateTrackingId() ;
				dozerMapper.map(req, saveData);
				saveData.setTrackingId(trackingId);
				saveData.setRequestReferenceNo(req.getRequestReferenceNo());
				saveData.setBranchCode(req.getBranchCode());
				saveData.setProductId(req.getProductId());
				saveData.setRequestReferenceNo(req.getRequestReferenceNo());
				saveData.setCreatedBy(req.getCreatedby());
				saveData.setOriginalPolicyNo(req.getOriginalPolicyNo());
				saveData.setEntryDate(today);
				saveData.setCompanyId(req.getCompanyId());
				saveData.setRemarks(req.getRemarks());
				saveData.setStatus(status);
				saveData.setStatusDesc(listItemValue.getItemValue());
				trackingrepo.saveAndFlush(saveData);
				log.info("Saved Details is ---> " + json.toJson(saveData));
				res.setResponse("Saved Successfully ");
				res.setSuccessId(trackingId);
			
		 }  catch(Exception e) {
			 e.printStackTrace();
			 log.info("Exception Is --->" + e.getMessage());
			 return null;
		 }
		return res;
	}

	public synchronized String generateTrackingId() {
	       try {
	    	   SeqTrackingId entity;
	            entity = seqTracRepo.save(new SeqTrackingId());    
	            String year =  sdfFormat.format(new Date()) ;
	            return "TRACK"+String.format("%05d",entity.getTrackingId()) ;
	        } catch (Exception e) {
				e.printStackTrace();
				log.info( "Exception is ---> " + e.getMessage());
	            return null;
	        }
	       
	 
	 }
}
