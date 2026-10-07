package com.maan.eway.common.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.Illustration;
import com.maan.eway.common.req.ShortQuote;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.service.impl.LifeShortQuoteService;
import com.maan.eway.error.Error;
import com.maan.eway.req.life.Life;

import io.swagger.annotations.Api;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shortquote")
@Api(tags = "Life Short Quote", description = "API's")
public class LifeShortQuoteController {
	@Autowired
	private LifeShortQuoteService service;
	
	
	@Autowired
	private IllustrationService illustration;
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/life")
	public ResponseEntity<CommonRes> calc( @Valid @RequestBody ShortQuote request,@RequestHeader("Authorization") String tokens){
		Map<String,Object> response=service.shortQuoteCalc(request,tokens);
		CommonRes data = new CommonRes();
		data.setCommonResponse(response);
		
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
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/illustration/{ProductId}")
	public ResponseEntity<CommonRes> calc(@PathVariable("ProductId")  String productId,@RequestBody Illustration OnetimeId){ 
		Object response=	illustration.generate(OnetimeId);
		CommonRes data = new CommonRes();
		data.setCommonResponse(response);
		
		if (data != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
			
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/illustration/downloadpdf/{ProductId}")
	public ResponseEntity<CommonRes> downloadPdf(@PathVariable("ProductId")  String productId,@RequestBody Illustration OnetimeId,@RequestHeader("Authorization") String tokens){ 
		Object response=	illustration.downloadPdf(OnetimeId,tokens);
		CommonRes data = new CommonRes();
		data.setCommonResponse(response);
		
		if (data != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/covert2quote")
	public ResponseEntity<CommonRes> converToQuote(@RequestBody Illustration request,@RequestHeader("Authorization") String tokens){
		Map<String,Object> response=service.converToQuote(request,tokens);
		CommonRes data = new CommonRes();
		data.setCommonResponse(response);
		
		if (data != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/createQuote")
	public ResponseEntity<CommonRes> createQuote( @RequestBody Life request,@RequestHeader("Authorization") String tokens){
		CommonRes data=service.createQuote(request,tokens);
		/*CommonRes data = new CommonRes();
		data.setCommonResponse(response);
		*/
		if (data != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/pushQuote")
	public ResponseEntity<CommonRes> pushQuote(@Valid @RequestBody Life request){
		Map<String,Object> response=service.pushQuote(request);
		CommonRes data = new CommonRes();
		data.setCommonResponse(response);
		
		if (data != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
