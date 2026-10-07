package com.maan.eway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.req.DMVICRequest;
import com.maan.eway.req.GetCertificateReq;
import com.maan.eway.req.ValidateKMPDCRequest;
import com.maan.eway.res.GetCertificateRes;
import com.maan.eway.res.ValidateDoubleInsuranceRes;
import com.maan.eway.service.DMVICService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/dmvic")
public class DMVICController {

	@Autowired
	private DMVICService dmvicService;

	@PostMapping(value = "/checkDMVICCall", produces = "application/json")
	@ApiOperation(value = "This method is DMVIC Call ")
	public ResponseEntity<?> checkDMVIC(@RequestBody DMVICRequest req) {
		ValidateDoubleInsuranceRes checkDMVIC = dmvicService.checkDMVIC(req);
		if (checkDMVIC != null) {
			return new ResponseEntity<>(checkDMVIC, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/validateKMPDC", produces = "application/json")
	@ApiOperation(value = "This method is to validate Doctors Registration No")
	public ResponseEntity<?> validateKMPDC(@RequestBody ValidateKMPDCRequest req) {
		ValidateDoubleInsuranceRes validateKMPDC = dmvicService.kmpdcValidation(req);
		if (validateKMPDC != null) {
			return new ResponseEntity<>(validateKMPDC, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/issueProfessionalDoctor", produces = "application/json")
	@ApiOperation(value = "This method is to validate Issue Professional Doctor Certificate")
	public ResponseEntity<?> issuePDCert(@RequestBody ValidateKMPDCRequest req) {
		ValidateDoubleInsuranceRes checkDMVIC = dmvicService.issuePDCert(req.getPolicyNo());
		System.out.println("DET: " + checkDMVIC);
		if (checkDMVIC != null) {
			return new ResponseEntity<>(checkDMVIC, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/getCertificate", produces = "application/json")
	@ApiOperation(value = "This method is to get  Doctor Certificate")
	public ResponseEntity<?> getCertificate(@RequestBody GetCertificateReq req) {
		GetCertificateRes checkDMVIC = dmvicService.getCertificate(req);
		System.out.println("DET: " + checkDMVIC);
		if (checkDMVIC != null) {
			return new ResponseEntity<>(checkDMVIC, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/vehicleSearch", produces = "application/json")
	@ApiOperation(value = "This method is DMVIC Call ")
	public ResponseEntity<?> vehicleSearchDMVIC(@RequestBody DMVICRequest req) {
		ValidateDoubleInsuranceRes checkDMVIC = dmvicService.vehicleSearchDMVIC(req);
		if (checkDMVIC != null) {
			return new ResponseEntity<>(checkDMVIC, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/getvehicleCertificate", produces = "application/json")
	@ApiOperation(value = "This method is DMVIC Call ")
	public ResponseEntity<?> getvehicleCertificate(@RequestBody ValidateKMPDCRequest req) {
		ValidateDoubleInsuranceRes checkDMVIC = dmvicService.vehicleCertificate(req);
		if (checkDMVIC != null) {
			return new ResponseEntity<>(checkDMVIC, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

}
