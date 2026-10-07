package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FidelityEmployeeDeleteReq {

	@JsonProperty("QuoteNo")
	 private String     quoteNo ;
	
	 @JsonProperty("RiskId")
	 private String    riskId ;
	 
	 @JsonProperty("EmployeeId")
	 private String    employeeId ;
}
