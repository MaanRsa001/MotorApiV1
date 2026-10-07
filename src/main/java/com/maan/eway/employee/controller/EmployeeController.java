package com.maan.eway.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.employee.bean.EmployeeDto;
import com.maan.eway.employee.service.EmployeeService;
import com.maan.eway.error.Error;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/employee")
@Api(tags = "EmployeeController", description = "API's")
public class EmployeeController {
	
	@Autowired
	private  EmployeeService service;
	
	@PostMapping("/insertEmployee")
	public ResponseEntity<CommonRes> waCancellationALL(@RequestBody EmployeeDto  request) {
		CommonRes data = new CommonRes();
		List<Error> validation = service.validateEnpDetails(request);
		//// validation
		if (validation != null && validation.size() != 0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(validation);
			data.setMessage("Failed");
			return new ResponseEntity<CommonRes>(data, HttpStatus.OK);

		} else {
			data= service.insertEmp(request);
		 	if (data != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
		 
	 	
	}
	
	@PostMapping("/getEmployee")
	public ResponseEntity<CommonRes> getEmployeeDetail(@RequestBody EmployeeDto  request) {
		CommonRes data = new CommonRes();
		
			data= service.getEmp(request);
		 	if (data != null) {
				return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		
		 
	 	
	}


}
