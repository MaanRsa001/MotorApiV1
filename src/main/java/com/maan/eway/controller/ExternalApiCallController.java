package com.maan.eway.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.req.TiraMsg;
import com.maan.eway.req.acknowledge.TiraM;
import com.maan.eway.req.acknowledge.TiraMsgAcknowlege;
import com.maan.eway.req.acknowledge.TiraMsgAcknowlegeFleet;
import com.maan.eway.req.fleet.TiraMsgVehiclePushFleet;
import com.maan.eway.req.push.TiraMsgCoverPush;
import com.maan.eway.req.push.TiraMsgVehiclePush;
import com.maan.eway.req.verification.TiraMsgVehicleVerification;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.MotorTiraMsgRes;
import com.maan.eway.res.NonMotorTiraMsgRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.verification.MotorTiraMsgVerificationRes;
import com.maan.eway.service.ExternalApiCallService;
import com.maan.eway.service.PrintReqService;
import com.maan.eway.service.impl.TiraFrameReqCall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "External : Details ", description = "API's")
@RequestMapping("/dispatch/api")
public class ExternalApiCallController {

	@Autowired
	private ExternalApiCallService externalService ;
	
	@Autowired
	private PrintReqService  reqPrinter ;
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/motor/verification/v1/request")
	@ApiOperation(value = "This method is to insert Customer")
	public ResponseEntity<CommonRes> savecustomer(@RequestBody TiraMsg req){
		CommonRes data = new CommonRes();
		reqPrinter.reqPrint(req);
		
		MotorTiraMsgRes res = externalService.getSampleData(req);
		data.setCommonResponse(res);
		data.setErrorMessage(Collections.emptyList());
		data.setIsError(false);
		data.setMessage("Success");
		
		if(res!=null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	} 
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")  // Motor Posting
	@PostMapping(value="/covernote/non-life/motor/v2/request",consumes = MediaType.APPLICATION_XML_VALUE,produces = MediaType.APPLICATION_XML_VALUE )
	@ApiOperation(value = "This method is Push Policy Information")
	//@JsonDeserialize //(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
	public ResponseEntity<MotorTiraMsgRes> pushVehicleInfo(@RequestBody TiraMsgVehiclePush req,@RequestHeader("Authorization") String tokens)throws Exception {
		MotorTiraMsgRes data = externalService.pushVehicleInfo(req,tokens);
		if(data!=null) {
			return new ResponseEntity<MotorTiraMsgRes>(data, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	 
	//@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')") // motor acknowlege
	@PostMapping(value="/covernote/non-life/motor/v2/acknowledge",produces = MediaType.APPLICATION_XML_VALUE)
	@ApiOperation(value = "This method is Push Policy Information")
	public ResponseEntity<TiraMsgAcknowlege> pushedVehicleAcknowledge(@RequestBody TiraMsgAcknowlege req){
		System.out.println("Response:"+req.toString());
		TiraMsgAcknowlege data =externalService.saveAcknowledge(req,"");
		if(data!=null) {
			return new ResponseEntity<TiraMsgAcknowlege>(data, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')") // verification
	@PostMapping(value="/ecovernote/api/covernote/verification/min/v1/request",consumes = MediaType.APPLICATION_XML_VALUE,produces = MediaType.APPLICATION_XML_VALUE )
	@ApiOperation(value = "This method is Push Policy Information")
	//@JsonDeserialize //(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
	public ResponseEntity<MotorTiraMsgVerificationRes> coverVerification(@RequestBody TiraMsgVehicleVerification req,@RequestHeader("Authorization") String tokens)throws Exception {
		MotorTiraMsgVerificationRes data = externalService.coverVerification(req,tokens,null);
		if(data!=null) {
			return new ResponseEntity<MotorTiraMsgVerificationRes>(data, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")
	@PostMapping(value="/ecovernote/api/covernote/non-life/other/v2/request",consumes = MediaType.APPLICATION_XML_VALUE,produces = MediaType.APPLICATION_XML_VALUE )
	@ApiOperation(value = "This method is Push Policy Information")
	//@JsonDeserialize //(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)	
	public ResponseEntity<NonMotorTiraMsgRes> pushNonMotorInfo(@RequestBody TiraMsgCoverPush req,@RequestHeader("Authorization") String tokens)throws Exception {
		System.out.println("Request:"+req.toString());
		NonMotorTiraMsgRes data = externalService.pushNonMotorInfo(req,tokens);
		if(data!=null) {
			return new ResponseEntity<NonMotorTiraMsgRes>(data, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	//@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')") // motor acknowlege
	@PostMapping(value="/ecovernote/api/covernote/non-life/other/v2/acknowledge",produces = MediaType.APPLICATION_XML_VALUE)
	@ApiOperation(value = "This method is Push Policy Information")
	public ResponseEntity<TiraMsgAcknowlege> pushedNonMotorAcknowledge(@RequestBody TiraMsgAcknowlege req){
		System.out.println("Response:"+req.toString());
		TiraMsgAcknowlege data =externalService.saveAcknowledgeForNonMotor(req,"");
		if(data!=null) {
			return new ResponseEntity<TiraMsgAcknowlege>(data, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	 
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER')")  // Motor Posting
	@PostMapping(value="/covernote/non-life/motor/v2/requestfleet",consumes = MediaType.APPLICATION_XML_VALUE,produces = MediaType.APPLICATION_XML_VALUE )
	@ApiOperation(value = "This method is Push Policy Information")
	//@JsonDeserialize //(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
	public ResponseEntity<MotorTiraMsgRes> pushVehicleInfoFleet(@RequestBody TiraMsgVehiclePushFleet req,@RequestHeader("Authorization") String tokens)throws Exception {
		MotorTiraMsgRes data = externalService.pushVehicleInfoFleet(req,tokens);
		if(data!=null) {
			return new ResponseEntity<MotorTiraMsgRes>(data, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/covernote/non-life/motor/v2/fleet/acknowledge",produces = MediaType.APPLICATION_XML_VALUE)
	@ApiOperation(value = "This method is Push Policy Information")
	public ResponseEntity<TiraM> pushedFleetMotorAcknowledge(@RequestBody TiraMsgAcknowlegeFleet req){
		System.out.println("Response:"+req.toString());
		TiraM data =externalService.saveAcknowledgeForFleetMotor(req,"");
		System.out.println("FLEET ACK Response:"+data);
		if(data!=null) {
			return new ResponseEntity<TiraM>(data, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_APPROVER','ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/pushpremia")
	@ApiOperation(value="This method is to Push Premia")
	public ResponseEntity<CommonRes> callTiraIntegeration(@RequestBody  TiraFrameReqCall req,@RequestHeader("Authorization") String tokens) {
		reqPrinter.reqPrint(req);
		CommonRes data = new CommonRes();
		
			SuccessRes res = externalService.callTiraIntegeration(req,tokens.replaceAll("Bearer ", "").split(",")[0]);
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
