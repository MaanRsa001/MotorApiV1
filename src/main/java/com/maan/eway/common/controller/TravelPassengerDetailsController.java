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
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.common.req.AddRemovedPassengerReq;
import com.maan.eway.common.req.PassengerSaveReq;
import com.maan.eway.common.req.TravelPassDetailsGetAllReq;
import com.maan.eway.common.req.TravelPassDetailsGetReq;
import com.maan.eway.common.req.TravelPassDetailsSaveListReq;
import com.maan.eway.common.req.TravelPassDetailsSaveReq;
import com.maan.eway.common.req.TravelPassValidateReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.TravelPassDetailsRes;
import com.maan.eway.common.res.TravelPassHistoryRes;
import com.maan.eway.common.service.TravelPassengerDetailsService;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.res.CommonErrorModuleReq;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.TravelPassCommonRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(  tags="Travel : Travel Passenger Details ", description = "API's")
@RequestMapping("/api")
public class TravelPassengerDetailsController {

	@Autowired
	private TravelPassengerDetailsService service;

	@Autowired
	private PrintReqService reqService;
	
	@Autowired
	private HomePositionMasterRepository homeRepo;
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
//
//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/updatepassdetails")
//	@ApiOperation("This method is to save  Passenger Details")
//	public ResponseEntity<CommonRes> savepassdetails(@RequestBody List<TravelPassDetailsSaveListReq> reqList) {
//		reqService.reqPrint(reqList);
//		CommonRes data = new CommonRes();
//
//		List<Error> validation = service.validatepassdetails(reqList);
//		if (validation != null && validation.size() > 0) {
//
//			data.setCommonResponse(null);
//			data.setErrorMessage(validation);
//			data.setMessage("Failed");
//			data.setIsError(true);
//			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
//		} else {
//
//			SuccessRes res = service.savepassdetails(reqList);
//			data.setCommonResponse(res);
//			data.setErrorMessage(Collections.EMPTY_LIST);
//			data.setIsError(false);
//			data.setMessage("Success");
//
//			if (res != null) {
//				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//			} else {
//				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//			}
//		}
//	}
//
//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/getpassdetails")
//	@ApiOperation("This method is to Get  Passenger Details")
//	public ResponseEntity<CommonRes> getpassdetails (@RequestBody TravelPassDetailsGetReq req){
//		reqService.reqPrint(req);
//		CommonRes data = new CommonRes();
//		
//		TravelPassDetailsRes res = service.getpassdetails(req);
//		data.setCommonResponse(res);
//		data.setErrorMessage(Collections.emptyList());
//		data.setIsError(false);
//		data.setMessage("Success");
//	
//		if(res!=null) {
//			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//		}
//		else {
//			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//		}
//	}
//	
//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/getallpassdetails")
//	@ApiOperation("This method is to Getall  Passenger Details")
//	public ResponseEntity<CommonRes> getallpassdetails(@RequestBody TravelPassDetailsGetAllReq req ){
//		CommonRes data = new CommonRes();
//		List<TravelPassDetailsRes> resList = service.getallpassdetails(req);
//		data.setCommonResponse(resList);
//		data.setErrorMessage(Collections.emptyList());
//		data.setIsError(false);
//		data.setMessage("Success");
//		if(resList!=null) {
//			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//		}
//		else {
//			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//		}
//	}
//	
//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/getallpasshistorydetails")
//	@ApiOperation("This method is to Getall  Passenger Details")
//	public ResponseEntity<CommonRes> getallpasshistorydetails(@RequestBody TravelPassDetailsGetAllReq req ){
//		CommonRes data = new CommonRes();
//		List<TravelPassHistoryRes> resList = service.getallpasshistorydetails(req);
//		data.setCommonResponse(resList);
//		data.setErrorMessage(Collections.emptyList());
//		data.setIsError(false);
//		data.setMessage("Success");
//		if(resList!=null) {
//			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//		}
//		else {
//			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//		}
//	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/deletepassenger")
	@ApiOperation("This method is to Delete  Passenger Details")
	public ResponseEntity<CommonRes> deletepassdetails (@RequestBody TravelPassDetailsGetReq req){
		reqService.reqPrint(req);
		CommonRes data = new CommonRes();
		
		SuccessRes res = service.deletepassdetails(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
	
		if(res!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/deleteallpassengers")
	@ApiOperation("This method is to Delete  All Passenger Details")
	public ResponseEntity<CommonRes> deleteallpassdetails (@RequestBody TravelPassDetailsGetAllReq req){
		reqService.reqPrint(req);
		CommonRes data = new CommonRes();
		
		SuccessRes res = service.deleteallpassdetails(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
	
		if(res!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/validatepassdetails")
//	@ApiOperation("This method is to Delete  Passenger Details")
//	public ResponseEntity<CommonRes> validatepassdetails (@RequestBody TravelPassValidateReq req){
//		reqService.reqPrint(req);
//		CommonRes data = new CommonRes();
//		
//		List<Error> validation = service.validatepassListDetails(req);
//		if (validation != null && validation.size() > 0) {
//
//			data.setCommonResponse(null);
//			data.setErrorMessage(validation);
//			data.setMessage("Failed");
//			data.setIsError(true);
//			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
//		} else {
//
//			TravelPassCommonRes res = service.getpassdetails(req);
//			data.setCommonResponse(res);
//			data.setErrorMessage(Collections.EMPTY_LIST);
//			data.setIsError(false);
//			data.setMessage("Success");
//
//			if (res != null) {
//				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//			} else {
//				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//			}
//		}
//	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/savepassengers")
	@ApiOperation("This method is to Save  Passenger List ")
	public ResponseEntity<CommonRes> savepassengers(@RequestBody PassengerSaveReq reqList) {
		reqService.reqPrint(reqList);
		CommonRes data = new CommonRes();

		List<String> validationCodes = service.mergepassergerValidation(reqList);
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
		CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
		String insuranceId ="";
		if(StringUtils.isNotBlank(reqList.getQuoteNo())) {
			HomePositionMaster homeData = homeRepo.findByQuoteNo(reqList.getQuoteNo()); 
			insuranceId = homeData.getCompanyId() ;
		}
		comErrDescReq.setBranchCode("99999");
		comErrDescReq.setInsuranceId(insuranceId);
		comErrDescReq.setProductId("4");
		comErrDescReq.setModuleId("4");
		comErrDescReq.setModuleName("ADDITIONAL INFO");
		
		validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
	}
		
		if (validation != null && validation.size() > 0) {

			data.setCommonResponse(null);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			data.setIsError(true);
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		} else {

			SuccessRes res = service.mergepassengerlist(reqList);
			data.setCommonResponse(res);
			data.setErrorMessage(Collections.EMPTY_LIST);
			data.setIsError(false);
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/proceedpassengers")
	@ApiOperation("This method is to Save & Proceed  Passenger List ")
	public ResponseEntity<CommonRes> proceedpassengers(@RequestBody PassengerSaveReq reqList) {
		reqService.reqPrint(reqList);
		CommonRes data = new CommonRes();

		List<String> validationCodes = service.addnewpassergerListValidate(reqList);
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
		CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
		String insuranceId ="";
		if(StringUtils.isNotBlank(reqList.getQuoteNo())) {
			HomePositionMaster homeData = homeRepo.findByQuoteNo(reqList.getQuoteNo()); 
			insuranceId = homeData.getCompanyId() ;
		}
		comErrDescReq.setBranchCode("99999");
		comErrDescReq.setInsuranceId(insuranceId);
		comErrDescReq.setProductId("4");
		comErrDescReq.setModuleId("4");
		comErrDescReq.setModuleName("ADDITIONAL INFO");
		
		validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
	}
		if (validation != null && validation.size() > 0) {

			data.setCommonResponse(null);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			data.setIsError(true);
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		} else {

			SuccessRes res = service.addpassengerlist(reqList);
			data.setCommonResponse(res);
			data.setErrorMessage(Collections.EMPTY_LIST);
			data.setIsError(false);
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/getactiverpassengers")
	@ApiOperation("This method is to Get Active Passenger List")
	public ResponseEntity<CommonRes> getActivePassengers(@RequestBody TravelPassDetailsGetAllReq req ){
		CommonRes data = new CommonRes();
		List<TravelPassDetailsRes> resList = service.getActivePassengers(req);
		data.setCommonResponse(resList);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if(resList!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/getremovedpassengers")
	@ApiOperation("This method is to Get Removed Passenger List")
	public ResponseEntity<CommonRes> getRemovedPassengers(@RequestBody TravelPassDetailsGetAllReq req ){
		CommonRes data = new CommonRes();
		List<TravelPassDetailsRes> resList = service.getRemovedPassengers(req);
		data.setCommonResponse(resList);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		if(resList!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/addagainremovedpassengers")
//	@ApiOperation("This method is to Again Add Removed Passengers")
//	public ResponseEntity<CommonRes> addRemovedPassengersAgain(@RequestBody AddRemovedPassengerReq req ){
//		CommonRes data = new CommonRes();
//		List<Error> validation = service.validateaddpassergerList(req);
//		if (validation != null && validation.size() > 0) {
//
//			data.setCommonResponse(null);
//			data.setErrorMessage(validation);
//			data.setMessage("Failed");
//			data.setIsError(true);
//			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
//		} else {
//
//			SuccessRes res = service.addRemovedPassengersAgain(req);
//			data.setCommonResponse(res);
//			data.setErrorMessage(Collections.EMPTY_LIST);
//			data.setIsError(false);
//			data.setMessage("Success");
//
//			if (res != null) {
//				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//			} else {
//				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//			}
//		}
//	}
}
