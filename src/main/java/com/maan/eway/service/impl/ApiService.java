package com.maan.eway.service.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.maan.eway.req.MotorVehicleInfoGetReq;
import com.maan.eway.req.TiraMsg;
import com.maan.eway.req.verification.TiraMsgVehicleVerification;
import com.maan.eway.res.MotorTiraMsgRes;
import com.maan.eway.res.MotorVehicleInfoRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.verification.MotorTiraMsgVerificationRes;
import com.maan.eway.service.ExternalApiCallService;
import com.maan.eway.service.RegulatoryInfoService;

@Service
public class ApiService {
    /*private final WebClient webClient=WebClient.builder().
    		baseUrl(null)
    		;
     */
	
	@Autowired
	private RegulatoryInfoService motorInfoService ;
	
	@Lazy
	@Autowired
	private ExternalApiCallService externalApiCallService;
    @Async
	public CompletableFuture<MotorVehicleInfoRes> coverVerification(TiraMsgVehicleVerification req,String tokens, MotorVehicleInfoGetReq request,TiraMsg tiraReq, String insuranceId) {
    	// Run both TIRA API calls in parallel instead of sequentially
    	CompletableFuture<MotorTiraMsgRes> sampleFuture =
    			CompletableFuture.supplyAsync(() -> externalApiCallService.getSampleData(tiraReq));

    	CompletableFuture<MotorTiraMsgVerificationRes> coverFuture =
    			CompletableFuture.supplyAsync(() -> externalApiCallService.coverVerification(req, tokens, request));

    	// Wait for both to complete
    	CompletableFuture.allOf(sampleFuture, coverFuture).join();

    	MotorTiraMsgRes sampleData = sampleFuture.join();
    	MotorTiraMsgVerificationRes coverVerification = coverFuture.join();

    	SuccessRes saveMotorInfo = motorInfoService.saveMotorInfo(tiraReq, sampleData, coverVerification, insuranceId);
    	MotorVehicleInfoRes response=null;
    	if(saveMotorInfo!=null) {
    		response = saveMotorInfo.getVehicle();
    	}

		return CompletableFuture.completedFuture(response);
    }
    
    @Async
    public CompletableFuture<MotorTiraMsgRes> callApi2Async(TiraMsg tiraReq, MotorVehicleInfoGetReq req) {    	
    	MotorTiraMsgRes sampleData = externalApiCallService.getSampleData(tiraReq);
    	return CompletableFuture.completedFuture(sampleData);
    }
	
    
}
