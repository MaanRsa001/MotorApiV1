package com.maan.eway.agri.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.agri.req.AgricultureMasterReq;
import com.maan.eway.agri.req.AgricultureMasterValidation;
import com.maan.eway.agri.req.agriGetreq;
import com.maan.eway.agri.req.cropDropDownReq;
import com.maan.eway.agri.res.CropDropdownRes;
import com.maan.eway.agri.res.SuccessResponse;
import com.maan.eway.agri.res.aezResponse;
import com.maan.eway.agri.res.cropDropRes;
import com.maan.eway.agri.service.AgricultureMasterService;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/agriculture")
@RequiredArgsConstructor
public class AgricultureMasterController {

	@Autowired
	private AgricultureMasterService service;
	
	 @Autowired
	    private AgricultureMasterValidation vali;

	@PostMapping("/saveOrUpdate")
	public ResponseEntity<CommonRes> saveOrUpdate(@RequestBody AgricultureMasterReq req) {
	    CommonRes data = new CommonRes();

	    List<Error> errors = vali.validateAgricultureMasterRequest(req);
	    if (errors != null && !errors.isEmpty()) {
	        data.setMessage("Failed to Save/Update");
	        data.setErroCode(105);
	        data.setIsError(true);
	        data.setErrorMessage(errors);
	        data.setCommonResponse(null);
	        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
	    } else {
	    	SuccessResponse response = service.saveorupdate(req);
	        data.setIsError(false);
	        data.setMessage("Saved/Updated Successfully");
	        data.setErroCode(110);
	        data.setErrorMessage(Collections.emptyList());
	        data.setCommonResponse(response);
	        return new ResponseEntity<>(data, HttpStatus.OK);
	    }
	}


	@GetMapping("/getAll/{companyId}/{productId}")
	public ResponseEntity<CommonRes> getAll(@PathVariable Integer companyId, @PathVariable Integer productId) {
		CommonRes response = new CommonRes();
		try {
			Object result = service.getAll(companyId, productId);
			response.setMessage("Fetched Successfully");
			response.setIsError(false);
			response.setCommonResponse(result);
			response.setErroCode(102);
		} catch (Exception e) {
			response.setMessage("Failed to Fetch");
			response.setIsError(true);
			response.setErroCode(501);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getById")
	public ResponseEntity<CommonRes> getById(@RequestBody agriGetreq req) {
		CommonRes response = new CommonRes();
		try {
			Object result = service.getById(req);
			response.setMessage("Fetched Successfully");
			response.setIsError(false);
			response.setCommonResponse(result);
			response.setErroCode(103);
		} catch (Exception e) {
			response.setMessage("Failed to Fetch");
			response.setIsError(true);
			response.setErroCode(502);
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getaez/{aez}")
	public ResponseEntity<CommonRes> getAllAez(@PathVariable Integer aez) {
		CommonRes response = new CommonRes();
		try {
			List<aezResponse> result = service.getAllAez(aez);
			response.setMessage("Fetched Successfully");
			response.setIsError(false);
			response.setCommonResponse(result);
			response.setErroCode(104);
		} catch (Exception e) {
			response.setMessage("Failed to Fetch");
			response.setIsError(true);
			response.setErroCode(503);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/crop-dropdown")
	public ResponseEntity<List<CropDropdownRes>> getCropDropdown() {
	    List<CropDropdownRes> data = service.getAllUniqueCropIdAndDesc();
	    return ResponseEntity.ok(data);
	}
	
	@PostMapping("/crop/dropdowns")
	public ResponseEntity<List<cropDropRes>> getCropDrop(@RequestBody cropDropDownReq req){
		List<cropDropRes> data=service.getCropDrop(req);
		return ResponseEntity.ok(data);
	}

}
