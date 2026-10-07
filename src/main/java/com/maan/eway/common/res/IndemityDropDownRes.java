package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data 
public class IndemityDropDownRes {
	
	@JsonProperty("ItemCode")
	private String itemCode;
	
	@JsonProperty("IntemType")
	private String itemType;
	
	@JsonProperty("InsurancedId")
	private String insuranceId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
}
