package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class BondMajorReq {
	@JsonProperty("RiskId")
	private Integer riskId;
	
	@JsonProperty("BondType")
	private String bondType;
	
	@JsonProperty("BondYear")
	private String bondYear;
		
	@JsonProperty("BondSuminsured")
	private String bondSumInsured;
}

