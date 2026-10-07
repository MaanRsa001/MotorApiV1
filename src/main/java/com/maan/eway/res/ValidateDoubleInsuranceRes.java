package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ValidateDoubleInsuranceRes {
	
	@JsonProperty("Response")
	private Object response;

	@JsonProperty("ErrorMessage")
	private String errorMessage;
	
	@JsonProperty("Message")
	private String massage;
	
	@JsonProperty("Success")
	private Boolean success;
	
	
	

}
