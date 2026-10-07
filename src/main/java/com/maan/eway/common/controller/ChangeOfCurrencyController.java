package com.maan.eway.common.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.ChangeOfCurrencyReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.SuccessRes;
import com.maan.eway.common.service.EserviceTravelDetailsService;
import com.maan.eway.common.service.impl.ChangeOfCurrencyServiceImpl;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api")
@Api(tags = "Change OF Currency", description = "API's")
public class ChangeOfCurrencyController {

	
	@Autowired
	private  ChangeOfCurrencyServiceImpl entityService;
	
	@Autowired
	private PrintReqService reqPrinter;
	
	
	// Change Of Currency
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value = "/update/changeofcurrencysi")
	@ApiOperation(value = "This method is to Update Change Of Currency Suminsured")
	public ResponseEntity<CommonRes> updateChangeOfCurrencySuminsured(@RequestBody ChangeOfCurrencyReq req) {
		CommonRes data = new CommonRes();

		// Save
		SuccessRes res = entityService.updateChangeOfCurrencySuminsured(req);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(data, HttpStatus.CREATED);
		}

	}
}
