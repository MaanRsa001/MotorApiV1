package com.maan.eway.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.EwayStateReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.service.EwayStateService;

@RestController
@RequestMapping("/state")
public class EwayStateMasterController {
	
	@Autowired
	private  EwayStateService service;
	
	@PostMapping("/getStateAndCity")
	public ResponseEntity<CommonRes> getStateAndRegion(@RequestBody  EwayStateReq req) {
		CommonRes data = new CommonRes();
		data = service.getStateAndRegionList(req);
		if (data.getCommonResponse() != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	@PostMapping("/getsuburbList")
	public ResponseEntity<CommonRes> getsuburbListfromCity(@RequestBody  EwayStateReq req) {
		CommonRes data = new CommonRes();
		data = service.getsuburbListfromCityId(req);
		if (data.getCommonResponse() != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		
	}

}
