package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPassValidateReq {


	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("RiskId")
	private String riskId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("LocationId")
	private String locationId;
}
