package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ValidateKMPDCRequest {
	
	@JsonProperty("PolicyNo")
	private String policyNo;
	
	@JsonProperty("RegistrationNumber")
	private String registrationNumber;

}
