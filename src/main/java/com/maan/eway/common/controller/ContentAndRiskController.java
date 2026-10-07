package com.maan.eway.common.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.common.req.AdditionalInfoGetReq;
import com.maan.eway.common.req.AdditionalInformationRequest;
import com.maan.eway.common.req.ContentAndRiskSaveReq;
import com.maan.eway.common.req.ContentRiskGetAllReq;
import com.maan.eway.common.req.ContentRiskGetReq;
import com.maan.eway.common.res.AdditionalInformationResponse;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.ContentRiskGetRes;
import com.maan.eway.common.res.ContentRiskGetallRes;
import com.maan.eway.common.service.ContentAndRiskService;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
import com.maan.eway.res.CommonErrorModuleReq;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "CONTENT AND RISK",description="API's")
public class ContentAndRiskController {

	@Autowired
	private ContentAndRiskService service;
	
	@Autowired
	private PrintReqService reqPrinter;
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
	@Autowired
	private EServiceBuildingDetailsRepository eserBuildingRepo ;

	@Autowired
	private MotorDataDetailsRepository motorRepo;
	
	@Autowired
	private HomePositionMasterRepository homeRepo ;
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value="/savecontentrisk")
	@ApiOperation(value="This method is to Save Content Risk")
	public ResponseEntity<CommonRes> savecontentrisk(@RequestBody ContentAndRiskSaveReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		
		List<String> validationCodes=  service.validatecontentrisk(req);
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			List<EserviceBuildingDetails> buidingData = new ArrayList<EserviceBuildingDetails>();
			//List<MotorDataDetails> motList = motorRepo.findByQuoteNoAndStatusNotOrderByVehicleIdAsc(req.getQuoteNo(),"D");
			List<MotorDataDetails> motList = motorRepo.findByRequestReferenceNoAndStatusNotOrderByVehicleIdAsc(req.getRequestReferenceNo(),"D");
			//buidingData = eserBuildingRepo.findByQuoteNoAndSectionId(req.getQuoteNo() , req.getSectionId());
			buidingData = eserBuildingRepo.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo() , req.getSectionId());

			String productId = req.getProductid();
			String companyId = req.getCompanyid();
			if(buidingData==null || buidingData.isEmpty()  ) {
				productId =  motList.size()==0 ? "5" : motList.get(0).getProductId().toString();
				companyId =  motList.size()==0 ? "" : motList.get(0).getCompanyId().toString();
			} else {
				companyId =  buidingData.get(0).getProductId();
				companyId =  buidingData.get(0).getCompanyId();
			}
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			comErrDescReq.setBranchCode("99999");
			comErrDescReq.setInsuranceId(req.getCompanyid());
			comErrDescReq.setProductId("99999");
			comErrDescReq.setModuleId("4");
			comErrDescReq.setModuleName("ADDITIONAL INFO");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		//Validation
		if(validation!=null && validation.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		}
		else {
			SuccessRes res = service.savecontentrisk(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if(res !=null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
		
	}

	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value="/getcontentrisk")
	@ApiOperation(value="This method is to Get get Content Risk")
	public ResponseEntity<CommonRes> getcontentrisk(@RequestBody ContentRiskGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		ContentRiskGetRes  res = service.getcontentrisk(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if(res !=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	

	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value="/getallcontentrisk")
	@ApiOperation(value="This method is to Getall Content Risk")
	public ResponseEntity<CommonRes> getallcontentrisk(@RequestBody ContentRiskGetAllReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		ContentRiskGetallRes  res = service.getallcontentrisk(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if(res !=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value="/savecontentitems")
	@ApiOperation(value="This method is to Save Content Risk")
	public ResponseEntity<CommonRes> savecontentitems(@RequestBody AdditionalInformationRequest req){
		CommonRes data = new CommonRes();
		
		List<String> validationCodes=  service.validateAdditionalInformation(req);
		List<Error> validation =null;
		
		HomePositionMaster home = homeRepo.findByQuoteNo(req.getQuoteNo());
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			comErrDescReq.setBranchCode(home.getBranchCode());
			comErrDescReq.setInsuranceId(home.getCompanyId());
			comErrDescReq.setProductId(String.valueOf(home.getProductId()));
			comErrDescReq.setModuleId("4");
			comErrDescReq.setModuleName("ADDITIONAL INFO");
			
			validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		//Validation
		if(validation!=null && validation.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		}
		else {
			AdditionalInformationResponse res = service.saveOrUpdate(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			if(res !=null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value="/getallAdditionalInfo")
	@ApiOperation(value="This method is to Getall Content Risk")
	public ResponseEntity<CommonRes> getByQuoteAndRef(@RequestBody AdditionalInfoGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		AdditionalInformationResponse  res = service.getByQuoteAndRef(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");
		if(res !=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(data, HttpStatus.CREATED);
		}
	}
	
	
	
}
