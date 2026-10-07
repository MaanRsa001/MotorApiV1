package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiaTiraReq {

	@JsonProperty("InsuranceId")
	private String insuranceId;
	
	@JsonProperty("PremiaCode")
	private String premiaCode;
}
