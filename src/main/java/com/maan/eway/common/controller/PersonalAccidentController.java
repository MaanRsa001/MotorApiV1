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

import com.maan.eway.common.req.PersonalAccidentGetAllReq;
import com.maan.eway.common.req.PersonalAccidentGetReq;
import com.maan.eway.common.req.PersonalAccidentSaveReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.PersonalAccidentGetAllRes;
import com.maan.eway.common.res.PersonalAccidentGetRes;
import com.maan.eway.common.service.PersonalAccidentService;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "PERSONAL ACCIDENT",description="API's")
public class PersonalAccidentController {

	@Autowired
	private PersonalAccidentService service;
	
	@Autowired
	private PrintReqService reqPrinter;
	
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value="/savepersonalaccident")
	@ApiOperation(value="This method is to Save Personal Accident")
	public ResponseEntity<CommonRes> savepersonalaccident(@RequestBody PersonalAccidentSaveReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<Error> validation =  service.validatepersonalaccident(req);
		
		
		  if(validation!=null && validation.size()!=0) { data.setCommonResponse(null);
		  data.setIsError(true); data.setErrorMessage(validation);
		  data.setMessage("Failed"); return new ResponseEntity<CommonRes>(data,
		  HttpStatus.OK); } else {
		 
			//SuccessRes res = service.savepersonalaccident(req);
			SuccessRes res = service.savepersonalaccidentInfo(req);
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
	@PostMapping(value="/getpersonalaccident")
	@ApiOperation(value="This method is to Get get Personal Accident")
	public ResponseEntity<CommonRes> getpersonalaccident(@RequestBody PersonalAccidentGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		PersonalAccidentGetRes  res = service.getpersonalaccident(req);
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
	@PostMapping(value="/getallpersonalaccident")
	@ApiOperation(value="This method is to Getall Personal Accident")
	public ResponseEntity<CommonRes> getallpersonalaccident(@RequestBody PersonalAccidentGetAllReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		PersonalAccidentGetAllRes  res = service.getallpersonalaccident(req);
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
