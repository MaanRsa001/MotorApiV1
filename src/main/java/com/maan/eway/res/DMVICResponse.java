package com.maan.eway.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DMVICResponse {
	
	@JsonProperty("Inputs")
	private String Inputs;
	
	@JsonProperty("Error")
	private List<DoubleInsuranceError> error;
	
	@JsonProperty("callbackObj")
	private DoubleInsuranceCallObj callbackObj;
		
	@JsonProperty("success")
	private Boolean success;
	
	@JsonProperty("APIRequestNumber")
	private String aPIRequestNumber;
	

}
