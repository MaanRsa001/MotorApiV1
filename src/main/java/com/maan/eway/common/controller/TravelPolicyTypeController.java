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

import com.maan.eway.common.req.TravelPassDetailsGetReq;
import com.maan.eway.common.req.TravelPolicyTypeGetReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.TravelPassDetailsRes;
import com.maan.eway.common.res.TravelPolicyTypeRes;
import com.maan.eway.common.service.TravelPolicyTypeService;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(  tags="Eservice Travel Policy Type : Travel Policy Type ", description = "API's")
@RequestMapping("/api")
public class TravelPolicyTypeController {

	@Autowired
	private TravelPolicyTypeService service;

	@Autowired
	private PrintReqService reqService;

	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/gettravelpolicytype")
	@ApiOperation("This method is to Get Travel Policy Type")
	public ResponseEntity<CommonRes> getTravelPolicyType (@RequestBody TravelPolicyTypeGetReq req){
		reqService.reqPrint(req);
		CommonRes data = new CommonRes();
		
		TravelPolicyTypeRes res = service.getTravelPolicyType(req);
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
	


}
