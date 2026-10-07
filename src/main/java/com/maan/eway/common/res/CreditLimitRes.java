package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreditLimitRes {
	
	@JsonProperty("Response")
	private Object response;

	@JsonProperty("ErrorMessage")
	private String errorMessage;
	
	@JsonProperty("Message")
	private String massage;
	
	@JsonProperty("Success")
	private Boolean success;
	
	@JsonProperty("CreditLimit")
	private Boolean creditLimit;

}
