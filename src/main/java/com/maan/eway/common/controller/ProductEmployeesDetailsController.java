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

import com.maan.eway.common.req.ProductEmployeeDeleteAllReq;
import com.maan.eway.common.req.ProductEmployeeDeleteReq;
import com.maan.eway.common.req.ProductEmployeesGetReq;
import com.maan.eway.common.req.SaveProductDetailsReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.ProductEmployeeGetRes;
import com.maan.eway.common.service.ProductEmployeesDetailsService;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.error.Error;
import com.maan.eway.res.CommonErrorModuleReq;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.service.PrintReqService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "PRODUCT EMPLOYEES DETAILS ",description="API's")
public class ProductEmployeesDetailsController {

	@Autowired
	private ProductEmployeesDetailsService service;
	
	@Autowired
	private PrintReqService reqPrinter;
	
	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/saveproductemployees")
//	@ApiOperation(value="This method is to Save Product Employees Details")
//	public ResponseEntity<CommonRes> saveProductDetails(@RequestBody SaveProductDetailsReq req){
//		reqPrinter.reqPrint(req);
//		CommonRes data = new CommonRes();
//		List<Error> validation =  service.validateProductEmployeesDetails(req);
//		//Validation
//		if(validation!=null && validation.size()!=0) {
//			data.setCommonResponse(null);
//			data.setIsError(true);
//			data.setErrorMessage(validation);
//			data.setMessage("Failed");
//			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
//		}
//		else {
//			SuccessRes res = service.saveProductEmployeesDetails(req);
//			data.setCommonResponse(res);
//			data.setIsError(false);
//			data.setErrorMessage(Collections.emptyList());
//			data.setMessage("Success");
//			if(res !=null) {
//				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//			}
//			else {
//				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//			}
//		}
//		
//	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/saveemployees")
	@ApiOperation(value="This method is to Save Product Employees Details")
	public ResponseEntity<CommonRes> saveEmployees(@RequestBody SaveProductDetailsReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes =  service.validateSaveEmployeesDetails(req);
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
		CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
		comErrDescReq.setBranchCode("99999");
		comErrDescReq.setInsuranceId(req.getInsuranceId());
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
			SuccessRes res = service.saveEmployeesDetails(req);
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
	@PostMapping("/proceedemployees")
	@ApiOperation(value="This method is to Save Product Employees Details")
	public ResponseEntity<CommonRes> proceedEmployees(@RequestBody SaveProductDetailsReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes =  service.validateProceedEmployeesDetails(req);
		//Validation
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
		CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
		comErrDescReq.setBranchCode("99999");
		comErrDescReq.setInsuranceId(req.getInsuranceId());
		comErrDescReq.setProductId("99999");
		comErrDescReq.setModuleId("4");
		comErrDescReq.setModuleName("ADDITIONAL INFO");
		
		validation = errorDescService.getErrorDesc(validationCodes ,comErrDescReq);
		}
		if(validation!=null && validation.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);
		}
		else {
			SuccessRes res = service.proceedEmployeesDetails(req);
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

//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/getallproductemployees")
//	@ApiOperation(value="This method is to Get Fidelity Employees Details")
//	public ResponseEntity<CommonRes> getallProductEmployeesDetails(@RequestBody ProductEmployeesGetReq req){
//		reqPrinter.reqPrint(req);
//		CommonRes data = new CommonRes();
//		List<ProductEmployeeGetRes>  res = service.getallProductEmployeesDetails(req);
//		data.setCommonResponse(res);
//		data.setIsError(false);
//		data.setErrorMessage(Collections.emptyList());
//		data.setMessage("Success");
//		if(res !=null) {
//			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//		}
//		else {
//			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//		}
//	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/getallactiveemployees")
	@ApiOperation(value="This method is to Get Fidelity Employees Details")
	public ResponseEntity<CommonRes> getallActiveEmployeesDetails(@RequestBody ProductEmployeesGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<ProductEmployeeGetRes>  res = service.getallActiveEmployeesDetails(req);
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
	@PostMapping("/getallremovedemployees")
	@ApiOperation(value="This method is to Get Fidelity Employees Details")
	public ResponseEntity<CommonRes> getallRemovedEmployeesDetails(@RequestBody ProductEmployeesGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<ProductEmployeeGetRes>  res = service.getallRemovedEmployeesDetails(req);
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
	@PostMapping("/deleteemployeebyid") //by emp id 
	@ApiOperation(value="This method is to Delete Fidelity Employees Details")
	public ResponseEntity<CommonRes> deleteFidelityEmployees(@RequestBody ProductEmployeeDeleteReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();

		
		SuccessRes res = service.deleteProductEmployeesDetails(req);
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
	@PostMapping("/deleteallemployees") //by emp id 
	@ApiOperation(value="This method is to Delete Fidelity Employees Details")
	public ResponseEntity<CommonRes> deleteAllFidelityEmployees(@RequestBody ProductEmployeesGetReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();

		
		SuccessRes res = service.deleteAllFidelityEmployees(req);
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
	@PostMapping("/validateproductemployeesdetailsexcel")
	@ApiOperation(value="This method is to validate Product Employees Details during Excel upload")
	public ResponseEntity<CommonRes> validateProductEmployeesDetailsExcel(@RequestBody SaveProductDetailsReq req){
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		List<String> validationCodes =  service.validateSaveEmployeesDetails(req);
		List<Error> validation =null;
		if(validationCodes!=null && validationCodes.size() > 0 ) {
		CommonErrorModuleReq comErrDescReq = new CommonErrorModuleReq();
		comErrDescReq.setBranchCode("99999");
		comErrDescReq.setInsuranceId(req.getInsuranceId());
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
			SuccessRes res = new SuccessRes();
			res.setResponse("No Errors");
			res.setSuccessId("");
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
//	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
//	@PostMapping("/deleteallproductemployees") //by emp id 
//	@ApiOperation(value="This method is to Delete All Employees Details")
//	public ResponseEntity<CommonRes> deleteAllEmployees(@RequestBody ProductEmployeeDeleteAllReq req){
//		reqPrinter.reqPrint(req);
//		CommonRes data = new CommonRes();
//
//		
//		SuccessRes res = service.deleteAllEmployees(req);
//		data.setCommonResponse(res);
//		data.setIsError(false);
//		data.setErrorMessage(Collections.emptyList());
//		data.setMessage("Success");
//		if(res !=null) {
//			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
//		}
//		else {
//			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//		}
//	}
	
	
}
