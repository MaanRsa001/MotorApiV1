package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DropDownReq {
	
//	@JsonProperty("ItemCode")
//	private String intemCode;
	
	@JsonProperty("IntemType")
	private String itemType;
	
	@JsonProperty("InsurancedId")
	private String insuranceId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	

}
