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
import com.maan.eway.common.req.EwayVehicleMakeModelGetReq;
import com.maan.eway.common.req.EwayVehicleMakeModelSaveReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.EwayVehicleMakeDropdownRes;
import com.maan.eway.common.res.EwayVehicleMakeModelGetRes;
import com.maan.eway.common.service.EwayVehicleMakemodelMasterDetailsService;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "EWAY VEHICLE MAKEMODEL MASTER DETAILS",description="API's")
public class EwayVehicleMakemodelMasterDetailController {

	@Autowired
	private EwayVehicleMakemodelMasterDetailsService service;
	
	@Autowired
	private PrintReqService reqPrinter;
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/vehiclemakedetails")
	@ApiOperation(value="This api is to get Vehicle Make")
	public ResponseEntity<CommonRes> getVehicleMake(@RequestBody EwayVehicleMakeModelGetReq req){
		CommonRes data = new CommonRes();
		List<EwayVehicleMakeDropdownRes> resList = service.getVehicleMake(req);
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
	@PostMapping("/vehiclemodeldetails")
	@ApiOperation(value="This method is to Get Vehicle Model Details")
	public ResponseEntity<CommonRes> getVehicleModel(@RequestBody EwayVehicleMakeModelGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<EwayVehicleMakeModelGetRes> resList = service.getVehicleModel(req);
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
	@PostMapping("/vehicletrimdetails")
	@ApiOperation(value="This method is to Get Vehicle Trim Details")
	public ResponseEntity<CommonRes> getVehicleTrim(@RequestBody EwayVehicleMakeModelGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<EwayVehicleMakeModelGetRes> resList = service.getVehicleTrim(req);
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
	// Insert
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
		@PostMapping("/savemotorbodytype")
		@ApiOperation(value = "This method is Save Make Motor ")

		public ResponseEntity<CommonRes> saveVehicleMakeModel(@RequestBody EwayVehicleMakeModelSaveReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();
			List<Error> validation = service.validateVehicleMakeModel(req);
			//// validation
			if (validation != null && validation.size() != 0) {
				data.setCommonResponse(null);
				data.setIsError(true);
				data.setErrorMessage(validation);
				data.setMessage("Failed");
				return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

			} else {
				/////// save

				SuccessRes res = service.saveVehicleMakeModel(req);
				data.setCommonResponse(res);
				data.setIsError(false);
				data.setErrorMessage(Collections.emptyList());
				data.setMessage("Success");
				if (res != null) {
					return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
			}

		}
		// Get By Vehicle Id
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
		@PostMapping("/getbyvehicleid")
		@ApiOperation(value = "This method is get by Make Id ")

		public ResponseEntity<CommonRes> getMakeId(@RequestBody EwayVehicleMakeModelGetReq req) {
			CommonRes data = new CommonRes();

			EwayVehicleMakeModelGetRes res = service.getByVehicleId(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}

		// Get All
		@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
		@PostMapping("/getallmotormake")
		@ApiOperation(value = "This method is Get all Motor Make ")

		public ResponseEntity<CommonRes> getallMotorMakeModelDetails(@RequestBody EwayVehicleMakeModelGetReq req) {
			reqPrinter.reqPrint(req);
			CommonRes data = new CommonRes();

			// Get All
			List<EwayVehicleMakeModelGetRes> res = service.getallMotorMakeModelDetails(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");

			if (res != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
	
}
