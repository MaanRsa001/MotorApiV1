package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriverListDetails {

	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("RiskId")
	private String riskId;
	
	@JsonProperty("DriverList")
	private List<DriverDetailsRes> driverList;
}
