package com.maan.eway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.CommonRes;
import com.maan.eway.service.OneTimeTableService;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/motor")
public class OneTimeTableController {

	@Autowired
	private  PrintReqService reqPrinter;
	
	@Autowired
	private  OneTimeTableService entityService ;
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/onetimetablesinsert")
	@ApiOperation(value = "This method is Insert Cover Details")
	public ResponseEntity<CommonRes> insertOneTimeTables(@RequestBody OneTimeTableReq req) {

		reqPrinter.reqPrint(req);
		// Save
		CommonRes res = entityService.insertOneTimeTables(req);
		
		if (res != null) {
			return new ResponseEntity<CommonRes>(res, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		

	}
}
