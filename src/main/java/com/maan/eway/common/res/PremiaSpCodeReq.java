package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiaSpCodeReq {

	@JsonProperty("InsuranceId")
	private String insuranceId;
	
	@JsonProperty("SpCode")
	private String spCode;
}
