package com.maan.eway.common.controller;

import java.util.List;
import java.util.stream.Collectors;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.ShortQuote;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.error.Error;
import com.maan.eway.service.impl.OneTimeServiceImpl;

import io.swagger.annotations.Api;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/onetime")
@Api(tags = "Life Short Quote", description = "API's")
public class OneTimeController {

	@Autowired
	private OneTimeServiceImpl service;
	@PostMapping("/life")
	public ResponseEntity<CommonRes> shortQuote(@Valid @RequestBody ShortQuote request){
		CommonRes data=service.saveLifeTables(request);
		if (data != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonRes> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> errorMessage = ex.getBindingResult().getFieldErrors();
        CommonRes data = new CommonRes();
        data.setIsError(true);        
        List<Error> list = errorMessage.stream().map( i-> new Error("","Request", i.getDefaultMessage())).collect(Collectors.toList());
        
        data.setErrorMessage(list);
        return ResponseEntity.badRequest().body(data);
    }
}
