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

import com.maan.eway.common.req.BurglaryOtherOccupantsGetReq;
import com.maan.eway.common.req.BurglaryOtherOccupantsSaveReq;
import com.maan.eway.common.res.BurglaryOtherOccupantsGetRes;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.service.BurglaryOtherOccupantsService;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "BURGLARY OTHER OCCUPANTS DETAILS",description="API's")
public class BurglaryOtherOccupantsController {

	@Autowired
	private BurglaryOtherOccupantsService service;
	
	@Autowired
	private PrintReqService reqPrinter;
	
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/saveburglaryotheroccupants")
	@ApiOperation(value="This method is to Save Burglary Details")
	public ResponseEntity<CommonRes> saveBurglaryOtherOccupants(@RequestBody List<BurglaryOtherOccupantsSaveReq> req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<Error> validation =  service.validateBurglaryOtherOccupants(req);
		//Validation
		if(validation!=null && validation.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		}
		else {
			SuccessRes res = service.saveBurglaryOtherOccupants(req);
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
	@PostMapping("/getburglaryquoteno")
	@ApiOperation(value="This method is to Save Burglary Details")
	public ResponseEntity<CommonRes> getBurglaryQuoteNo(@RequestBody BurglaryOtherOccupantsGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		BurglaryOtherOccupantsGetRes res = service.getBurglaryQuoteNo(req);
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
