package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DoubleInsuranceError {
	
	@JsonProperty("errorCode")
	private String errorCode;
	
	@JsonProperty("errorText")
	private String errorText;

}
