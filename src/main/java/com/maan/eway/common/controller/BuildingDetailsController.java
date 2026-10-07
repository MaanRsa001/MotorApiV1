package com.maan.eway.common.controller;

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
import org.apache.commons.lang3.StringUtils;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.FactorRateRequestDetails;
import com.maan.eway.common.req.AdditionalValidationReq;
import com.maan.eway.common.req.BuildingDetailsGetAllReq;
import com.maan.eway.common.req.BuildingDetailsGetReq;
import com.maan.eway.common.req.BuildingDetailsSaveReq;
import com.maan.eway.common.res.BuildingDetailsGetRes;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.service.BuildingDetailsService;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.res.CommonErrorModuleReq;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.SuccessRes1;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "BUILDING DETAILS",description="API's")
public class BuildingDetailsController {

	@Autowired
	private BuildingDetailsService service;
	
	@Autowired
	private PrintReqService reqPrinter;
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
	@Autowired
	private EServiceBuildingDetailsRepository eserBuildingRepo ;
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/buildingdetails")
	@ApiOperation(value="This method is to Save Building Details")
	public ResponseEntity<CommonRes> savebuildingDetails(@RequestBody List<BuildingDetailsSaveReq> req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes =  service.validatebuildingDetails(req);
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			String sectionId = "0";
		//	EserviceBuildingDetails eserBuildingData = eserBuildingRepo.findByQuoteNoAndSectionId(req.get(0).getQuoteNo(),sectionId);
			List<EserviceBuildingDetails> eserBuildingData = eserBuildingRepo.findByRequestReferenceNoAndSectionId(req.get(0).getRequestReferenceNo(),sectionId);

			comErrDescReq.setBranchCode("99999");
			comErrDescReq.setInsuranceId(eserBuildingData.get(0).getCompanyId());
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
			SuccessRes1 res = service.savebuildingDetails(req);
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
	@PostMapping("/getbuildingdetails")
	@ApiOperation(value="This method is to Get Building Details")
	public ResponseEntity<CommonRes> getbuildingDetails(@RequestBody BuildingDetailsGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<BuildingDetailsGetRes> res = service.getbuildingDetails(req);
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
	@PostMapping("/getallbuildingdetails")
	@ApiOperation(value="This method is to Getall Building Details")
	public ResponseEntity<CommonRes> getallbuildingDetails(@RequestBody BuildingDetailsGetAllReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();

		
		List<BuildingDetailsGetRes>  res = service.getallbuildingDetails(req);
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
	
	
	@PostMapping("/deletebuildingdetails")
	@ApiOperation(value="This method is to Getall Building Details")
	public ResponseEntity<CommonRes> deletebuildingDetails(@RequestBody BuildingDetailsGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();

		
		SuccessRes res = service.deleteBuildingDetails(req);
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
	
	//Domestic and corporate plus additional info validation
	@PostMapping("/additionalinfovali")
	@ApiOperation(value="This method is to Validate Additional Details")
	public ResponseEntity<CommonRes> additionalInfoVali(@RequestBody AdditionalValidationReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();

		List<String> validationCodes =  service.additionalInfoVali(req);
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
			CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
			String companyId="";
			String sectionId = "0";
			if(StringUtils.isNotBlank(req.getQuoteNo())) {
			EserviceBuildingDetails eserBuildingData = eserBuildingRepo.findByQuoteNoAndSectionId(req.getQuoteNo(),sectionId);
			companyId=eserBuildingData.getCompanyId();
			}
			comErrDescReq.setBranchCode("99999");
			comErrDescReq.setInsuranceId(companyId);
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
		} else {
			data.setCommonResponse(null);
			data.setIsError(false);
			data.setMessage("Success");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		}
	}
	
	
}
